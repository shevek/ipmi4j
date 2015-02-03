/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.global;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;
import javax.annotation.Nonnegative;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiCommandHandler;
import org.anarres.ipmi.protocol.packet.common.Bits;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiResponse;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiSession;

/**
 * [IPMI2] Section 20.1, table 20-2, page 244.
 *
 * @author shevek
 */
public class GetDeviceIdResponse extends AbstractIpmiResponse {

    public static enum DeviceSupport implements Bits.Wrapper {

        Chassis(7),
        Bridge(6),
        IPMBEventGenerator(5),
        IPMBEventReceiver(4),
        FRUInventoryDevice(3),
        SELDevice(2),
        SDRRepositoryDevice(1),
        SensorDevice(0);
        private final Bits bits;

        private DeviceSupport(@Nonnegative int index) {
            this.bits = Bits.forBitIndex(0, index);
        }

        @Override
        public Bits getBits() {
            return bits;
        }
    }
    public byte deviceId;
    public boolean deviceProvidesDeviceSDRs;
    public int deviceRevision;
    public boolean deviceAvailable;
    public int deviceFirmwareRevisionMajor;
    public int deviceFirmwareRevisionMinor;
    public int deviceIpmiRevision;
    public Set<DeviceSupport> deviceSupport = EnumSet.noneOf(DeviceSupport.class);
    public int oemEnterpriseNumber;
    public char productId;
    public Integer auxiliaryFirmwareRevisionInformation;

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetDeviceID;
    }

    @Override
    public void apply(IpmiClientIpmiCommandHandler handler, IpmiSession session) {
        handler.handleGetDeviceIdResponse(session, this);
    }

    @Override
    protected int getResponseDataWireLength() {
        return auxiliaryFirmwareRevisionInformation == null ? 12 : 16;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        if (toWireCompletionCode(buffer))   // 1
            return;
        buffer.put(deviceId);   // 2
        buffer.put(setBit((byte) (deviceRevision & 0xF), 7, deviceProvidesDeviceSDRs)); // 3
        buffer.put(setBit((byte) (deviceFirmwareRevisionMajor & 0x7F), 7, deviceAvailable));    // 4
        buffer.put(toWireBcdLE(UnsignedBytes.checkedCast(deviceFirmwareRevisionMinor)));   // 5
        buffer.put(toWireBcdLE(UnsignedBytes.checkedCast(deviceIpmiRevision)));    // 6
        buffer.put(Bits.toByte(deviceSupport)); // 7
        toWireOemIanaLE3(buffer, oemEnterpriseNumber); // 8:10
        toWireCharLE(buffer, productId);
        if (auxiliaryFirmwareRevisionInformation != null)
            buffer.putInt(auxiliaryFirmwareRevisionInformation);
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        if (fromWireCompletionCode(buffer)) // 1
            return;
        deviceId = buffer.get();    // 2
        byte b = buffer.get();  // 3
        deviceProvidesDeviceSDRs = getBit(b, 7);
        deviceRevision = b & 0xF;
        b = buffer.get();   // 4
        deviceAvailable = getBit(b, 7);
        deviceFirmwareRevisionMajor = b & 0x7F;
        deviceFirmwareRevisionMinor = fromWireBcdLE(buffer.get());  // 5
        deviceIpmiRevision = fromWireBcdLE(buffer.get());   // 6
        deviceSupport = Bits.fromBuffer(DeviceSupport.class, buffer, 1);    // 7
        oemEnterpriseNumber = fromWireOemIanaLE3(buffer);  // 8:10
        productId = fromWireCharLE(buffer); // 11:12
        if (buffer.remaining() >= 4)
            auxiliaryFirmwareRevisionInformation = buffer.getInt();
        else
            auxiliaryFirmwareRevisionInformation = null;
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "DeviceId", "0x" + UnsignedBytes.toString(deviceId, 16));
        appendValue(buf, depth, "DeviceAvailable", deviceAvailable);
        appendValue(buf, depth, "ManufacturerIanaEnterpriseNumber", toStringOemIana(oemEnterpriseNumber));
        appendValue(buf, depth, "ProductId", productId);
        appendValue(buf, depth, "DeviceRevision", "0x" + Integer.toHexString(deviceRevision));
        appendValue(buf, depth, "DeviceFirmwareRevision", deviceFirmwareRevisionMajor + "." + deviceFirmwareRevisionMinor);
        appendValue(buf, depth, "AuxiliaryFirmwareRevisionInformation", auxiliaryFirmwareRevisionInformation);
        appendValue(buf, depth, "DeviceIpmiRevision", deviceIpmiRevision);  // divide by 10? 20 = 2.0, 15 = 1.5
        appendValue(buf, depth, "DeviceSupport", deviceSupport);
        appendValue(buf, depth, "DeviceProvidesDeviceSDRs", deviceProvidesDeviceSDRs);
    }
}
