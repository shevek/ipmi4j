/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * IPMI Packet Header.
 * 
 * This implementation is a little heavy in object allocations, but it
 * does guarantee a very strong type contract. It could be made faster and
 * cheaper if volume becomes a problem.
 *
 * @author shevek
 */
public class IpmiHeader extends AbstractWireable {

    private byte targetAddress;
    private IpmiLun targetLun;
    private byte sourceAddress;
    private IpmiLun sourceLun;
    private IpmiSequenceNumber sequenceNumber;
    private IpmiCommand command;

    public byte getTargetAddress() {
        return targetAddress;
    }

    public IpmiLun getTargetLun() {
        return targetLun;
    }

    public byte getSourceAddress() {
        return sourceAddress;
    }

    public IpmiLun getSourceLun() {
        return sourceLun;
    }

    public IpmiSequenceNumber getSequenceNumber() {
        return sequenceNumber;
    }

    public IpmiCommand getCommand() {
        return command;
    }

    @Override
    public int getWireLength() {
        return 6;
    }

    @Override
    protected void toWireUnchecked(ByteBuffer buffer) {
        buffer.put(getTargetAddress());
        buffer.put((byte) (getCommand().getNetworkFunction().getCode() << 2 | getTargetLun().getValue()));
        buffer.put((byte) 0);  // TODO: Header checksum
        buffer.put(getSourceAddress());
        buffer.put((byte) (getSequenceNumber().getValue() << 2 | getSourceLun().getValue()));
        buffer.put(getCommand().getCode());
    }

    @Override
    protected void fromWireUnchecked(ByteBuffer buffer) {
        byte tmp;
        targetAddress = buffer.get();
        tmp = buffer.get();
        IpmiNetworkFunction networkFunction = Code.fromByte(IpmiNetworkFunction.class, (byte) (tmp >>> 2));
        targetLun = new IpmiLun(tmp & IpmiLun.MASK);
        buffer.get();   // TOOD: Header checksum
        sourceAddress = buffer.get();
        tmp = buffer.get();
        sequenceNumber = new IpmiSequenceNumber(tmp >>> 2);
        sourceLun = new IpmiLun(tmp & IpmiLun.MASK);
        command = IpmiCommand.fromByte(networkFunction, buffer.get());
    }
}
