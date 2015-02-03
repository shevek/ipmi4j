/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sensor;

import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiCommandHandler;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiSession;

/**
 * [IPMI2] Section 35.9, table 35-9, page 459.
 *
 * @author shevek
 */
public class GetSensorThresholdRequest extends AbstractIpmiGetSensorRequest {

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetSensorThreshold;
    }

    @Override
    public void apply(IpmiClientIpmiCommandHandler handler, IpmiSession session) {
        handler.handleGetSensorThresholdRequest(session, this);
    }
}