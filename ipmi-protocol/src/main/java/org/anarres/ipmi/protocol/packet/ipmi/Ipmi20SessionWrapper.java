/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import com.google.common.base.Throwables;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayloadType;
import com.google.common.primitives.Chars;
import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.IanaEnterpriseNumber;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.common.Pad;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiCommand;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiAuthenticationAlgorithm;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiConfidentialityAlgorithm;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiSession;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiSessionManager;

/**
 * [IPMI2] Section 13.6 pages 133-134, column 3.
 * Confidentiality wrappers from [IPMI2] Section 13.29 page 160, table 13-20.
 *
 * @author shevek
 */
public class Ipmi20SessionWrapper extends AbstractIpmiSessionWrapper {

    private final IpmiSessionAuthenticationType authenticationType = IpmiSessionAuthenticationType.RMCPP;
    // private IpmiPayloadType payloadType;
    // private boolean encrypted;
    // private boolean authenticated;
    private IanaEnterpriseNumber oemEnterpriseNumber;    // 3 byte oem iana; 1 byte zero
    private char oemPayloadId;
    // private int ipmiSessionId;
    // private int ipmiSessionSequenceNumber;
    // private byte[] confidentialityHeader;
    // private byte[] confidentialityTrailer;
    // private byte[] integrityData;

    // @Override public int getIpmiSessionId() { return ipmiSessionId; }
    // @Override public int getIpmiSessionSequenceNumber() { return ipmiSessionSequenceNumber; }
    @Nonnegative
    private int getPayloadLength(@Nonnull IpmiSession session, @Nonnull IpmiPayload payload) {
        try {
            return session.getConfidentialityAlgorithm().getWireLength(session, payload);
        } catch (NoSuchAlgorithmException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public int getWireLength(IpmiSession session, IpmiPayload payload) {
        boolean oem = IpmiPayloadType.OEM_EXPLICIT.equals(payload.getPayloadType());
        int payloadLength = getPayloadLength(session, payload);
        return 1 // authenticationType
                + 1 // payloadType
                + (oem ? 4 : 0) // oemEnterpriseNumber
                + (oem ? 2 : 0) // oemPayloadId
                + 4 // ipmiSessionId
                + 4 // ipmiSessionSequenceNumber
                + 2 // payloadLength
                + payloadLength
                + Pad.PAD(payloadLength).length
                + 1 // pad length
                + 1 // next header
                + session.getIntegrityAlgorithm().getMacLength();
    }

    /** Sequence number handling: [IPMI2] Section 6.12.13, page 59. */
    @Override
    public void toWire(ByteBuffer buffer, IpmiSession session, IpmiPayload payload) {
        try {
            boolean encrypted = !IpmiConfidentialityAlgorithm.NONE.equals(session.getConfidentialityAlgorithm());
            boolean authenticated = !IpmiAuthenticationAlgorithm.RAKP_NONE.equals(session.getAuthenticationAlgorithm());

            // Page 133
            buffer.put(authenticationType.getCode());
            byte payloadTypeByte = payload.getPayloadType().getCode();
            payloadTypeByte = AbstractIpmiCommand.setBit(payloadTypeByte, 7, encrypted);
            payloadTypeByte = AbstractIpmiCommand.setBit(payloadTypeByte, 6, authenticated);
            buffer.put(payloadTypeByte);
            if (IpmiPayloadType.OEM_EXPLICIT.equals(payload.getPayloadType())) {
                buffer.putInt(oemEnterpriseNumber.getNumber() << Byte.SIZE);
                buffer.putChar(oemPayloadId);
            }
            buffer.putInt(session.getId());
            int ipmiSessionSequenceNumber = encrypted ? session.nextEncryptedSequenceNumber() : session.nextUnencryptedSequenceNumber();
            buffer.putInt(ipmiSessionSequenceNumber);

            ByteBuffer integrityInput = buffer.duplicate();

            // Page 134
            // 2 byte payload length
            int payloadLength = getPayloadLength(session, payload);
            buffer.putChar(Chars.checkedCast(payloadLength));
            session.getConfidentialityAlgorithm().toWire(buffer, session, payload);

            // Integrity padding.
            byte[] pad = Pad.PAD(payloadLength);
            buffer.put(pad);
            buffer.put((byte) pad.length);
            buffer.put((byte) 0x07); // Reserved, [IPMI2] Page 134, next-header field.

            integrityInput.limit(buffer.position());
            byte[] integrityData = session.getIntegrityAlgorithm().sign(session, integrityInput);
            buffer.put(integrityData);
        } catch (GeneralSecurityException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void fromWire(ByteBuffer buffer, IpmiSessionManager sessionManager, IpmiSessionData sessionData) {
        try {
            AbstractWireable.assertWireByte(buffer, authenticationType.getCode(), "IPMI session authentication type");
            byte payloadTypeByte = buffer.get();
            boolean encrypted = AbstractIpmiCommand.getBit(payloadTypeByte, 7);
            boolean authenticated = AbstractIpmiCommand.getBit(payloadTypeByte, 6);
            IpmiPayloadType payloadType = Code.fromInt(IpmiPayloadType.class, payloadTypeByte & 0x3F);

            int sessionId = buffer.getInt();
            IpmiSession session = sessionManager.getSession(sessionId);
            sessionData.setIpmiSession(session);

            int sessionSequenceNumber = buffer.getInt();

            ByteBuffer integrityInput = buffer.duplicate();

            // TODO: Before calling payload.fromWire(), make sure to set the position and limit on the buffer.
            int payloadLength = buffer.getChar();
            ByteBuffer payloadEncrypted = buffer.duplicate();
            payloadEncrypted.limit(payloadEncrypted.position() + payloadLength);
            buffer.position(payloadEncrypted.limit());

            ByteBuffer payloadBuffer = encrypted ? session.getConfidentialityAlgorithm().fromWire(payloadEncrypted, session) : payloadEncrypted;
            IpmiPayload payload = newPayload(payloadBuffer, payloadType);
            payload.fromWire(payloadBuffer);
            sessionData.setIpmiPayload(payload);

            int integrityPadLength = Pad.PAD(payloadLength).length;
            byte[] integrityPad = AbstractWireable.readBytes(buffer, integrityPadLength);
            // TODO: Assert integrityPad all 0xFF.

            AbstractWireable.assertWireByte(buffer, UnsignedBytes.checkedCast(integrityPadLength), "Integrity pad length");
            AbstractWireable.assertWireByte(buffer, (byte) 0x07, "Next-header field");

            integrityInput.limit(buffer.position());
            byte[] integrityData = session.getIntegrityAlgorithm().sign(session, integrityInput);
            // TODO: Assert integrityData equal to outstanding buffer data.
        } catch (GeneralSecurityException e) {
            throw Throwables.propagate(e);
        }
    }
}