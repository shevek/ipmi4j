/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.messaging;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiCommandHandler;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiRequest;
import org.anarres.ipmi.protocol.packet.ipmi.payload.RequestedMaximumPrivilegeLevel;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiSession;

/**
 * [IPMI2] Section 22.18, table 22-23, page 297.
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
    public void apply(IpmiClientIpmiCommandHandler handler, IpmiSession session) {
        handler.handleSetSessionPrivilegeLevelRequest(session, this);
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

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "PrivilegeLevel", privilegeLevel);
    }
}