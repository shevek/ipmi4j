/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sel;

import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiCommandHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiSimpleRequest;

/**
 * [IPMI2] Section 31.2, table 31-2, page 423.
 *
 * @author shevek
 */
public class GetSELInfoRequest extends AbstractIpmiSimpleRequest {

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetSELInfo;
    }

    @Override
    public void apply(IpmiClientIpmiCommandHandler handler, IpmiHandlerContext context) {
        handler.handleGetSELInfoRequest(context, this);
    }
}