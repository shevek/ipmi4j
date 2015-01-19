/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.messaging;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiRequest;
import org.anarres.ipmi.protocol.packet.ipmi.payload.RequestedMaximumPrivilegeLevel;

/**
 *
 * @author shevek
 */
public class SetSessionPrivilegeLevelRequest extends AbstractIpmiRequest {

    public RequestedMaximumPrivilegeLevel privilegeLevel;

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.SetSessionPrivilegeLevel;
    }

    @Override
    protected int getDataWireLength() {
        return 1;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        buffer.put(privilegeLevel.getCode());
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        privilegeLevel = Code.fromBuffer(RequestedMaximumPrivilegeLevel.class, buffer);
    }
}
