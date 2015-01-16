/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command;

import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;

/**
 *
 * @author shevek
 */
public class UnknownIpmiCommand extends AbstractIpmiCommand {

    private final IpmiCommandName commandName;
    private byte[] data;

    public UnknownIpmiCommand(@Nonnull IpmiCommandName commandName) {
        this.commandName = commandName;
    }

    @Override
    public IpmiCommandName getCommandName() {
        return commandName;
    }

    @Override
    protected int getDataWireLength() {
        return data.length;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        buffer.put(data);
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        data = readBytes(buffer, buffer.remaining());
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "Data (unknown)", toHexString(data));
    }
}
