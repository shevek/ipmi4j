/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.global;

import org.anarres.ipmi.protocol.client.IpmiClientCommandHandler;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiSimpleRequest;

/**
 * [IPMI2] Section 20.1, table 20-2, page 244.
 *
 * @author shevek
 */
public class GetDeviceIdRequest extends AbstractIpmiSimpleRequest {

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetDeviceID;
    }

    @Override
    public void apply(IpmiClientCommandHandler handler) {
        handler.handleGetDeviceIdRequest(this);
    }
}