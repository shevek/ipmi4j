/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.chassis;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiCommandHandler;
import org.anarres.ipmi.protocol.packet.common.Bits;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.IPMBAddress;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiResponse;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiSession;

/**
 *
 * @author shevek
 */
public class GetChassisCapabilitiesResponse extends AbstractIpmiResponse {

    public static enum Capabilities implements Bits.DescriptiveWrapper {

        ProvidesPowerInterlock(3, "Provides power interlock (IPMI 1.5)"),
        ProvidesDiagnosticInterrupt(2, "Provides Diagnostic Interrupt (FP NMI) (IPMI 1.5)"),
        /** This indicates that the chassis has capabilities to lock out external power control and reset button or front panel interfaces and/or detect tampering with those interfaces. */
        ProvidesFrontPanelLockout(1, "Provides 'Front Panel Lockout'"),
        ProvidesIntrusionSensor(0, "Chassis provides intrusion (physical security) sensor");
        private final Bits bits;
        private final String description;
        /* pp */ Capabilities(int bit, String description) {
            this.bits = Bits.forBitIndex(0, bit);
            this.description = description;
        }

        @Override
        public Bits getBits() {
            return bits;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return Bits.Utils.toString(this);
        }
    }
    public Set<Capabilities> chassisCapabilities = EnumSet.noneOf(Capabilities.class);
    public IPMBAddress chassisFruInfoDeviceAddress;
    public IPMBAddress chassisSdrDeviceAddress;
    public IPMBAddress chassisSelDeviceAddress;
    public IPMBAddress chassisSystemManagementDeviceAddress;
    public IPMBAddress chassisBridgeDeviceAddress = IPMBAddress.Addresss20;

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetChassisCapabilities;
    }

    @Override
    public void apply(IpmiClientIpmiCommandHandler handler, IpmiSession session) {
        handler.handleGetChassisCapabilitiesResponse(session, this);
    }

    @Override
    protected int getResponseDataWireLength() {
        return IPMBAddress.Addresss20.equals(chassisBridgeDeviceAddress) ? 6 : 7;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        if (toWireCompletionCode(buffer))
            return;
        buffer.put(chassisFruInfoDeviceAddress.getCode());
        buffer.put(chassisSdrDeviceAddress.getCode());
        buffer.put(chassisSelDeviceAddress.getCode());
        buffer.put(chassisSystemManagementDeviceAddress.getCode());
        if (!IPMBAddress.Addresss20.equals(chassisBridgeDeviceAddress))
            buffer.put(chassisBridgeDeviceAddress.getCode());
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        if (fromWireCompletionCode(buffer))
            return;
        chassisFruInfoDeviceAddress = Code.fromBuffer(IPMBAddress.class, buffer);
        chassisSdrDeviceAddress = Code.fromBuffer(IPMBAddress.class, buffer);
        chassisSelDeviceAddress = Code.fromBuffer(IPMBAddress.class, buffer);
        chassisSystemManagementDeviceAddress = Code.fromBuffer(IPMBAddress.class, buffer);
        if (buffer.hasRemaining())
            chassisBridgeDeviceAddress = Code.fromBuffer(IPMBAddress.class, buffer);
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "ChassisCapabilities", chassisCapabilities);
        appendValue(buf, depth, "ChassisFruInfoDeviceAddress", chassisFruInfoDeviceAddress);
        appendValue(buf, depth, "ChassisSdrDeviceAddress", chassisSdrDeviceAddress);
        appendValue(buf, depth, "ChassisSelDeviceAddress", chassisSelDeviceAddress);
        appendValue(buf, depth, "ChassisSystemManagementDeviceAddress", chassisSystemManagementDeviceAddress);
        appendValue(buf, depth, "ChassisBridgeDeviceAddress", chassisBridgeDeviceAddress);
    }
}
