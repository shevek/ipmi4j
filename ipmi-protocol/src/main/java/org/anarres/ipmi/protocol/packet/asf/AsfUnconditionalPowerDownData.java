/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientAsfMessageHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;

/**
 * UnconditionalPowerDown.
 * 
 * http://www.dmtf.org/sites/default/files/standards/documents/DSP0136.pdf
 * http://www.dmtf.org/standards/asf
 * Section 3.2.4.2 page 35.
 *
 * @author shevek
 */
public class AsfUnconditionalPowerDownData extends AbstractAsfData {

    @Override
    public AsfRmcpMessageType getMessageType() {
        return AsfRmcpMessageType.UnconditionalPowerDown;
    }

    @Override
    public void apply(IpmiClientAsfMessageHandler handler, IpmiHandlerContext context) {
        handler.handleAsfUnconditionalPowerDownData(context, this);
    }

    @Override
    protected int getDataWireLength() {
        return 0;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
    }
}
