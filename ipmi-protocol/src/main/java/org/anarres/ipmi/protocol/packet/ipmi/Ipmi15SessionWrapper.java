/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.common.Wireable;

/**
 * [IPMI2] Section 13.6 pages 133-134, column 1.
 *
 * @author shevek
 */
public class Ipmi15SessionWrapper implements IpmiSessionWrapper {

    private IpmiHeaderAuthenticationType authenticationType = IpmiHeaderAuthenticationType.NONE;
    private int ipmiSessionSequenceNumber;
    private int ipmiSessionId;
    private byte[] ipmiMessageAuthenticationCode;   // 16 bytes

    @Override
    public int getIpmiSessionId() {
        return ipmiSessionId;
    }

    @Override
    public int getIpmiSessionSequenceNumber() {
        return ipmiSessionSequenceNumber;
    }

    private class Header extends AbstractWireable {

        @Override
        public int getWireLength() {
            return 1 + 4 + 4 + (authenticationType != IpmiHeaderAuthenticationType.NONE ? 16 : 0);
        }

        @Override
        protected void toWireUnchecked(ByteBuffer buffer) {
            // Page 133
            buffer.put(authenticationType.getCode());
            buffer.putInt(ipmiSessionSequenceNumber);
            buffer.putInt(ipmiSessionId);
            if (authenticationType != IpmiHeaderAuthenticationType.NONE)
                buffer.put(ipmiMessageAuthenticationCode);
            // Page 134
            // 1 byte payload length
        }

        @Override
        protected void fromWireUnchecked(ByteBuffer buffer) {
            authenticationType = Code.fromBuffer(IpmiHeaderAuthenticationType.class, buffer);
            ipmiSessionSequenceNumber = buffer.getInt();
            ipmiSessionId = buffer.getInt();
            if (authenticationType != IpmiHeaderAuthenticationType.NONE)
                ipmiMessageAuthenticationCode = readBytes(buffer, 16);
            else
                ipmiMessageAuthenticationCode = null;
        }
    }

    @Override
    public Wireable getIpmiSessionHeader(IpmiData data) {
        return new Header();
    }

    // TODO: Legacy pad only.
    private static class Trailer extends AbstractWireable {

        private static final Trailer INSTANCE = new Trailer();

        @Override
        public int getWireLength() {
            return 0;
        }

        @Override
        protected void toWireUnchecked(ByteBuffer buffer) {
        }

        @Override
        protected void fromWireUnchecked(ByteBuffer buffer) {
        }
    }

    @Override
    public Wireable getIpmiSessionTrailer(IpmiData data) {
        return Trailer.INSTANCE;
    }
}
