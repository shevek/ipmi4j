/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.chassis;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiSessionRequest;

/**
 * [IPMI2] Section 28.2, table 28-3, page 389.
 *
 * @author shevek
 */
public class GetChassisStatusRequest extends AbstractIpmiSessionRequest {

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetChassisStatus;
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
