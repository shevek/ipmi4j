/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import com.google.common.base.Throwables;
import com.google.common.primitives.Chars;
import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiCommand;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayloadType;
import org.anarres.ipmi.protocol.packet.ipmi.payload.OemExplicit;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiAuthenticationAlgorithm;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiConfidentialityAlgorithm;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiIntegrityAlgorithm;
import org.anarres.ipmi.protocol.packet.ipmi.security.impl.integrity.IntegrityPad;
import org.anarres.ipmi.protocol.client.session.IpmiPacketContext;
import org.anarres.ipmi.protocol.client.session.IpmiSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * [IPMI2] Section 13.6 pages 133-134, column 3.
 * Confidentiality wrappers from [IPMI2] Section 13.29 page 160, table 13-20.
 *
 * @author shevek
 */
public class Ipmi20SessionWrapper extends AbstractIpmiSessionWrapper {

    private static final Logger LOG = LoggerFactory.getLogger(Ipmi20SessionWrapper.class);
    private static final IpmiSessionAuthenticationType AUTHENTICATION_TYPE = IpmiSessionAuthenticationType.RMCPP;
    // private IpmiPayloadType payloadType;
    private boolean encrypted;
    private boolean authenticated;

    // private IanaEnterpriseNumber oemEnterpriseNumber;    // 3 byte oem iana; 1 byte zero
    // private char oemPayloadId;
    @Override
    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    @Nonnull
    public IpmiConfidentialityAlgorithm getConfidentialityAlgorithm(@CheckForNull IpmiSession session) {
        if (!isEncrypted())
            return IpmiConfidentialityAlgorithm.NONE;
        if (getIpmiSessionId() == 0)
            return IpmiConfidentialityAlgorithm.NONE;
        if (session == null)
            throw new IllegalStateException("Confidentiality requested, but cannot find session " + Integer.toHexString(getIpmiSessionId()));
        return session.getConfidentialityAlgorithm();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    @Nonnull
    public IpmiAuthenticationAlgorithm getAuthenticationAlgorithm(@CheckForNull IpmiSession session) {
        if (!isEncrypted())
            return IpmiAuthenticationAlgorithm.RAKP_NONE;
        if (getIpmiSessionId() == 0)
            return IpmiAuthenticationAlgorithm.RAKP_NONE;
        if (session == null)
            throw new IllegalStateException("Authentication requested, but cannot find session " + Integer.toHexString(getIpmiSessionId()));
        return session.getAuthenticationAlgorithm();
    }

    @Override
    public int getWireLength(IpmiPacketContext context) {
        try {
            @CheckForNull
            IpmiSession session = context.getIpmiSession(getIpmiSessionId());

            IpmiConfidentialityAlgorithm confidentialityAlgorithm = getConfidentialityAlgorithm(session);
            IpmiIntegrityAlgorithm integrityAlgorithm = getIntegrityAlgorithm(session);

            IpmiPayload payload = getIpmiPayload();

            boolean oem = IpmiPayloadType.OEM_EXPLICIT.equals(payload.getPayloadType());
            int unencryptedLength = payload.getWireLength(context);
            int encryptedLength = confidentialityAlgorithm.getEncryptedLength(session, unencryptedLength);
            int integrityLength = (session == null)
                    ? 0
                    : IntegrityPad.PAD(encryptedLength).length
                    + 1 // pad length
                    + 1 // next header
                    + integrityAlgorithm.getMacLength();
            return 1 // authenticationType
                    + 1 // payloadType
                    + (oem ? 4 : 0) // oemEnterpriseNumber
                    + (oem ? 2 : 0) // oemPayloadId
                    + 4 // ipmiSessionId
                    + 4 // ipmiSessionSequenceNumber
                    + 2 // payloadLength
                    + encryptedLength
                    + integrityLength;
        } catch (NoSuchAlgorithmException e) {
            throw Throwables.propagate(e);
        }
    }

    /** Sequence number handling: [IPMI2] Section 6.12.13, page 59. */
    @Override
    public void toWireUnchecked(IpmiPacketContext context, ByteBuffer buffer) {
        try {
            @CheckForNull
            IpmiSession session = context.getIpmiSession(getIpmiSessionId());

            IpmiConfidentialityAlgorithm confidentialityAlgorithm = getConfidentialityAlgorithm(session);
            IpmiAuthenticationAlgorithm authenticationAlgorithm = getAuthenticationAlgorithm(session);
            IpmiIntegrityAlgorithm integrityAlgorithm = getIntegrityAlgorithm(session);

            boolean encrypted = !IpmiConfidentialityAlgorithm.NONE.equals(confidentialityAlgorithm);
            boolean authenticated = !IpmiAuthenticationAlgorithm.RAKP_NONE.equals(authenticationAlgorithm);

            IpmiPayload payload = getIpmiPayload();

            // Page 133
            buffer.put(AUTHENTICATION_TYPE.getCode());
            byte payloadTypeByte = payload.getPayloadType().getCode();
            payloadTypeByte = AbstractIpmiCommand.setBit(payloadTypeByte, 7, encrypted);
            payloadTypeByte = AbstractIpmiCommand.setBit(payloadTypeByte, 6, authenticated);
            buffer.put(payloadTypeByte);
            if (IpmiPayloadType.OEM_EXPLICIT.equals(payload.getPayloadType())) {
                OemExplicit oemPayload = (OemExplicit) payload;
                buffer.putInt(oemPayload.getOemEnterpriseNumber() << Byte.SIZE);
                buffer.putChar(oemPayload.getOemPayloadId());
            }
            toWireIntLE(buffer, getIpmiSessionId());
            toWireIntLE(buffer, getIpmiSessionSequenceNumber());

            ByteBuffer integrityInput = buffer.duplicate();

            // Page 134
            // 2 byte payload length
            int unencryptedLength = payload.getWireLength(context);
            int encryptedLength = confidentialityAlgorithm.getEncryptedLength(session, unencryptedLength);
            toWireCharLE(buffer, Chars.checkedCast(encryptedLength));

            ENCRYPT:
            {
                if (encrypted) {
                    ByteBuffer unencryptedBuffer = ByteBuffer.allocate(unencryptedLength);
                    payload.toWire(context, unencryptedBuffer);
                    unencryptedBuffer.flip();
                    confidentialityAlgorithm.encrypt(session, buffer, unencryptedBuffer);
                } else {
                    // Avoidance of allocation optimization.
                    payload.toWire(context, buffer);
                }
            }

            SIGN:
            if (!IpmiIntegrityAlgorithm.NONE.equals(integrityAlgorithm)) {
                // Integrity padding.
                byte[] pad = IntegrityPad.PAD(encryptedLength);
                buffer.put(pad);
                buffer.put((byte) pad.length);
                buffer.put((byte) 0x07); // Reserved, [IPMI2] Page 134, next-header field.

                integrityInput.limit(buffer.position());
                byte[] integrityData = integrityAlgorithm.sign(session, integrityInput);
                buffer.put(integrityData);
            }
        } catch (GeneralSecurityException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void fromWireUnchecked(IpmiPacketContext context, ByteBuffer buffer) {
        try {
            AbstractWireable.assertWireByte(buffer, AUTHENTICATION_TYPE.getCode(), "IPMI session authentication type");
            byte payloadTypeByte = buffer.get();
            encrypted = AbstractIpmiCommand.getBit(payloadTypeByte, 7);
            authenticated = AbstractIpmiCommand.getBit(payloadTypeByte, 6);
            IpmiPayloadType payloadType = Code.fromInt(IpmiPayloadType.class, payloadTypeByte & 0x3F);

            int sessionId = fromWireIntLE(buffer);
            setIpmiSessionId(sessionId);
            setIpmiSessionSequenceNumber(fromWireIntLE(buffer));

            IpmiSession session = context.getIpmiSession(sessionId);
            IpmiAuthenticationAlgorithm authenticationAlgorithm = getAuthenticationAlgorithm(session);
            IpmiConfidentialityAlgorithm confidentialityAlgorithm = getConfidentialityAlgorithm(session);
            IpmiIntegrityAlgorithm integrityAlgorithm = getIntegrityAlgorithm(session);

            ByteBuffer integrityInput = buffer.duplicate();

            // TODO: Before calling payload.fromWire(), make sure to set the position and limit on the buffer.
            int payloadLength = fromWireCharLE(buffer);
            ByteBuffer encryptedBuffer = buffer.duplicate();
            // LOG.info("position=" + encryptedBuffer.position());
            // LOG.info("length=0x" + Integer.toHexString(payloadLength));
            encryptedBuffer.limit(encryptedBuffer.position() + payloadLength);
            buffer.position(encryptedBuffer.limit());

            DECRYPT:
            {
                ByteBuffer unencryptedBuffer = isEncrypted()
                        ? session.getConfidentialityAlgorithm().decrypt(session, encryptedBuffer)
                        : encryptedBuffer;
                IpmiPayload payload = newPayload(unencryptedBuffer, payloadType);
                payload.fromWire(context, unencryptedBuffer);
                setIpmiPayload(payload);

                if (unencryptedBuffer.hasRemaining()) {
                    throw new IllegalStateException(unencryptedBuffer.remaining()
                            + " bytes remaining in buffer after decoding " + payload);
                }
            }

            SIGN:
            if (!IpmiIntegrityAlgorithm.NONE.equals(integrityAlgorithm)) {
                int integrityPadLength = IntegrityPad.PAD(payloadLength).length;
                // LOG.info("IntegrityPad length is " + integrityPadLength);
                byte[] integrityPad = AbstractWireable.readBytes(buffer, integrityPadLength);
                // TODO: Assert integrityPad all 0xFF.

                AbstractWireable.assertWireByte(buffer, UnsignedBytes.checkedCast(integrityPadLength), "Integrity pad length");
                AbstractWireable.assertWireByte(buffer, (byte) 0x07, "Next-header field");

                integrityInput.limit(buffer.position());
                byte[] integrityData = session.getIntegrityAlgorithm().sign(session, integrityInput);
                // TODO: Assert integrityData equal to outstanding buffer data.
            }
        } catch (GeneralSecurityException e) {
            throw Throwables.propagate(e);
        }
    }
}