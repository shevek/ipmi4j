/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import com.google.common.primitives.Chars;
import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.IanaEnterpriseNumber;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;
import org.anarres.ipmi.protocol.packet.common.Pad;
import org.anarres.ipmi.protocol.packet.common.Wireable;

/**
 * [IPMI2] Section 13.6 pages 133-134, column 3.
 *
 * @author shevek
 */
public class Ipmi20SessionWrapper implements IpmiSessionWrapper {

    private IpmiHeaderAuthenticationType authenticationType;
    private IpmiPayloadType payloadType;
    private boolean encrypted;
    private boolean authenticated;
    private IanaEnterpriseNumber oemEnterpriseNumber;    // 3 byte oem iana; 1 byte zero
    private char oemPayloadId;
    private int ipmiSessionId;
    private int ipmiSessionSequenceNumber;
    private byte[] confidentialityHeader;
    private byte[] confidentialityTrailer;
    private byte[] integrityData;

    @Override
    public int getIpmiSessionId() {
        return ipmiSessionId;
    }

    @Override
    public int getIpmiSessionSequenceNumber() {
        return ipmiSessionSequenceNumber;
    }

    @Nonnegative
    private int getPayloadLength(@Nonnull IpmiData data) {
        return confidentialityHeader.length
                + data.getIpmiHeader().getWireLength()
                + data.getDataWireLength()
                + confidentialityTrailer.length;
    }

    private class Header extends AbstractWireable {

        private final IpmiData data;

        public Header(@Nonnull IpmiData data) {
            this.data = data;
        }

        @Override
        public int getWireLength() {
            boolean oem = IpmiPayloadType.OEM_EXPLICIT.equals(payloadType);
            return 1 // authenticationType
                    + 1 // payloadType
                    + (oem ? 4 : 0) // oemEnterpriseNumber
                    + (oem ? 2 : 0) // oemPayloadId
                    + 4 // ipmiSessionId
                    + 4 // ipmiSessionSequenceNumber
                    + 2 // payloadLength
                    + confidentialityHeader.length
                    + confidentialityTrailer.length;
        }

        // 2 byte payload length
        @Override
        protected void toWireUnchecked(ByteBuffer buffer) {
            // Page 133
            buffer.put(authenticationType.getCode());
            byte payloadTypeByte = payloadType.getCode();
            if (encrypted)
                payloadTypeByte |= 0x80;
            if (authenticated)
                payloadTypeByte |= 0x40;
            buffer.put(payloadTypeByte);
            if (IpmiPayloadType.OEM_EXPLICIT.equals(payloadType)) {
                buffer.putInt(oemEnterpriseNumber.getNumber() << Byte.SIZE);
                buffer.putChar(oemPayloadId);
            }
            buffer.putInt(ipmiSessionId);
            buffer.putInt(ipmiSessionSequenceNumber);

            // Page 134
            // 2 byte payload length
            int payloadLength = getPayloadLength(data);
            buffer.putChar(Chars.checkedCast(payloadLength));
            buffer.put(confidentialityHeader);
        }

        @Override
        protected void fromWireUnchecked(ByteBuffer buffer) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Override
    public Wireable getIpmiSessionHeader(IpmiData data) {
        return new Header(data);
    }

    private class Trailer extends AbstractWireable {

        private final IpmiData data;

        public Trailer(@Nonnull IpmiData data) {
            this.data = data;
        }

        @Override
        public int getWireLength() {
            int payloadLength = getPayloadLength(data);
            byte[] pad = Pad.PAD(payloadLength);
            return pad.length
                    + 1 // pad length
                    + 1 // next header
                    + integrityData.length;
        }

        @Override
        protected void toWireUnchecked(ByteBuffer buffer) {
            int payloadLength = getPayloadLength(data);
            byte[] pad = Pad.PAD(payloadLength);
            buffer.put(pad);
            buffer.put((byte) pad.length);
            buffer.put((byte) 0x07); // Reserved, [IPMI2] Page 134
            buffer.put(integrityData);
        }

        @Override
        protected void fromWireUnchecked(ByteBuffer buffer) {
            int payloadLength = getPayloadLength(data);
            byte[] pad = Pad.PAD(payloadLength);
            readBytes(buffer, pad.length);
            assertWireByte(buffer, (byte) pad.length, "padding length");
            assertWireByte(buffer, (byte) 0x07, "IPMI v2.0 next header byte (reserved as 0x07)");
            integrityData = readBytes(buffer, integrityData.length);
        }
    }

    @Override
    public Wireable getIpmiSessionTrailer(IpmiData data) {
        return new Trailer(data);
    }
}