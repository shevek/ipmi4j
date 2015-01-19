/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;

/**
 * For requests with no data fields beyond the {@link IpmiCommandName}.
 *
 * @author shevek
 */
public abstract class AbstractIpmiSimpleRequest extends AbstractIpmiRequest {

    @Override
    protected final int getDataWireLength() {
        return 0;
    }

    @Override
    protected final void toWireData(ByteBuffer buffer) {
    }

    @Override
    protected final void fromWireData(ByteBuffer buffer) {
    }
}
