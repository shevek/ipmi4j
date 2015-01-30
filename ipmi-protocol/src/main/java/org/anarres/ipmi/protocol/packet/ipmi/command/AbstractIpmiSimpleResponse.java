/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;

/**
 * For responses with no data fields beyond the {@link IpmiCommandName}
 * and the {@link #getIpmiCompletionCode() completion code}.
 *
 * @author shevek
 */
public abstract class AbstractIpmiSimpleResponse extends AbstractIpmiResponse {

    @Override
    protected int getResponseDataWireLength() {
        return 1;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        if (toWireCompletionCode(buffer))
            return;
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        if (fromWireCompletionCode(buffer))
            return;
    }
}
