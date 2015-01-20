/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.global;

import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiSimpleRequest;

/**
 *
 * @author shevek
 */
public class GetDeviceIdRequest extends AbstractIpmiSimpleRequest {

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetDeviceID;
    }
}