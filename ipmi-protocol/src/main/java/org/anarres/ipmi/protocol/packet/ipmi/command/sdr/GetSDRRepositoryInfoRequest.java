/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sdr;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiRequest;

/**
 * [IPMI2] Section 33.9, table 33-3, page 441.
 *
 * @author shevek
 */
public class GetSDRRepositoryInfoRequest extends AbstractIpmiRequest {

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetSDRRepositoryInfo;
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
