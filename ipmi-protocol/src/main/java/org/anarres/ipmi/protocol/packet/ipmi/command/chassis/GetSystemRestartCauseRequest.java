/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.chassis;

import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiCommandHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiSimpleRequest;

/**
 * [IPMI2] Section 28.11, table 28-11, page 394.
 *
 * @author shevek
 */
public class GetSystemRestartCauseRequest extends AbstractIpmiSimpleRequest {

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetSystemRestartCause;
    }

    @Override
    public void apply(IpmiClientIpmiCommandHandler handler, IpmiHandlerContext context) {
        handler.handleGetSystemRestartCauseRequest(context, this);
    }
}
