/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import com.google.common.base.Throwables;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayloadType;
import com.google.common.primitives.Chars;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.IanaEnterpriseNumber;
import org.anarres.ipmi.protocol.packet.common.Pad;
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
public class Ipmi20SessionWrapper implements IpmiSessionWrapper {

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

    @Override
    public void toWire(ByteBuffer buffer, IpmiSession session, IpmiPayload payload) {
        try {
            boolean encrypted = !IpmiConfidentialityAlgorithm.NONE.equals(session.getConfidentialityAlgorithm());
            boolean authenticated = !IpmiAuthenticationAlgorithm.RAKP_NONE.equals(session.getAuthenticationAlgorithm());

            // Page 133
            buffer.put(authenticationType.getCode());
            byte payloadTypeByte = payload.getPayloadType().getCode();
            if (encrypted)
                payloadTypeByte |= 0x80;
            if (authenticated)
                payloadTypeByte |= 0x40;
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

    /*
     @Override
     protected void fromWireUnchecked(ByteBuffer buffer) {
     int payloadLength = getPayloadLength(payload);
     byte[] pad = Pad.PAD(payloadLength);
     AbstractWireable.readBytes(buffer, pad.length);
     AbstractWireable.assertWireByte(buffer, (byte) pad.length, "padding length");
     AbstractWireable.assertWireByte(buffer, (byte) 0x07, "IPMI v2.0 next header byte (reserved as 0x07)");
     integrityData = AbstractWireable.readBytes(buffer, integrityData.length);
     }
     */
    @Override
    public IpmiSession fromWire(ByteBuffer buffer, IpmiSessionManager session, IpmiPayload payload) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}