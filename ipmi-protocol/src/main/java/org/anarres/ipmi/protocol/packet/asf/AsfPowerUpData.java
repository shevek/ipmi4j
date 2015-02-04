/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import org.anarres.ipmi.protocol.client.visitor.IpmiClientAsfMessageHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;

/**
 * PowerUp.
 * 
 * http://www.dmtf.org/sites/default/files/standards/documents/DSP0136.pdf
 * http://www.dmtf.org/standards/asf
 * Section 3.2.4.1 page 33.
 *
 * @author shevek
 */
public class AsfPowerUpData extends AbstractAsfBootData {

    @Override
    public AsfRmcpMessageType getMessageType() {
        return AsfRmcpMessageType.PowerUp;
    }

    @Override
    public void apply(IpmiClientAsfMessageHandler handler, IpmiHandlerContext context) {
        handler.handleAsfPowerUpData(context, this);
    }
}
