/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sol;

import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiCommandHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiConfigurationParametersResponse;

/**
 * [IPMI2] Section 26.3, table 26-4, page 376.
 *
 * @author shevek
 */
public class GetSOLConfigurationParametersResponse extends AbstractIpmiConfigurationParametersResponse {

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetSOLConfigurationParameters;
    }

    @Override
    public void apply(IpmiClientIpmiCommandHandler handler, IpmiHandlerContext context) {
        handler.handleGetSOLConfigurationParametersResponse(context, this);
    }
}