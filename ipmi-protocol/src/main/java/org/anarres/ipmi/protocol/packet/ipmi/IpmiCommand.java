/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import com.google.common.primitives.UnsignedBytes;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * [IPMI2] Appendix G, pages 591-596.
 *
 * @author shevek
 */
public enum IpmiCommand implements Code.Wrapper {
// IPM Device 'Global' Commands

    // reserved("reserved", IpmiNetworkFunction.App, 0x00),
    GetDeviceID("Get Device ID", IpmiNetworkFunction.App, 0x01),
    BroadcastGetDeviceID("Broadcast 'Get Device ID'", IpmiNetworkFunction.App, 0x01),
    ColdReset("Cold Reset", IpmiNetworkFunction.App, 0x02),
    WarmReset("Warm Reset", IpmiNetworkFunction.App, 0x03),
    GetSelfTestResults("Get Self Test Results", IpmiNetworkFunction.App, 0x04),
    ManufacturingTestOn("Manufacturing Test On", IpmiNetworkFunction.App, 0x05),
    SetACPIPowerState("Set ACPI Power State", IpmiNetworkFunction.App, 0x06),
    GetACPIPowerState("Get ACPI Power State", IpmiNetworkFunction.App, 0x07),
    GetDeviceGUID("Get Device GUID", IpmiNetworkFunction.App, 0x08),
    GetNetFnSupport("Get NetFn Support", IpmiNetworkFunction.App, 0x09),
    GetCommandSupport("Get Command Support", IpmiNetworkFunction.App, 0x0A),
    GetCommandSubFunctionSupport("Get Command Sub-function Support", IpmiNetworkFunction.App, 0x0B),
    GetConfigurableCommands("Get Configurable Commands", IpmiNetworkFunction.App, 0x0C),
    GetConfigurableCommandSubFunctions("Get Configurable Command Sub-functions", IpmiNetworkFunction.App, 0x0D),
    // unassigned("unassigned", IpmiNetworkFunction.App, 0x0Eh-0F),
    SetCommandEnables("Set Command Enables", IpmiNetworkFunction.App, 0x60),
    GetCommandEnables("Get Command Enables", IpmiNetworkFunction.App, 0x61),
    SetCommandSubFunctionEnables("Set Command Sub-function Enables", IpmiNetworkFunction.App, 0x62),
    GetCommandSubFunctionEnables("Get Command Sub-function Enables", IpmiNetworkFunction.App, 0x63),
    GetOEMNetFnIANASupport("Get OEM NetFn IANA Support", IpmiNetworkFunction.App, 0x64),
    // BMC Watchdog Timer Commands
    ResetWatchdogTimer("Reset Watchdog Timer", IpmiNetworkFunction.App, 0x22),
    SetWatchdogTimer("Set Watchdog Timer", IpmiNetworkFunction.App, 0x24),
    GetWatchdogTimer("Get Watchdog Timer", IpmiNetworkFunction.App, 0x25),
    // BMC Device and Messaging Commands
    SetBMCGlobalEnables("Set BMC Global Enables", IpmiNetworkFunction.App, 0x2E),
    GetBMCGlobalEnables("Get BMC Global Enables", IpmiNetworkFunction.App, 0x2F),
    ClearMessageFlags("Clear Message Flags", IpmiNetworkFunction.App, 0x30),
    GetMessageFlags("Get Message Flags", IpmiNetworkFunction.App, 0x31),
    EnableMessageChannelReceive("Enable Message Channel Receive", IpmiNetworkFunction.App, 0x32),
    GetMessage("Get Message", IpmiNetworkFunction.App, 0x33),
    SendMessage("Send Message", IpmiNetworkFunction.App, 0x34),
    ReadEventMessageBuffer("Read Event Message Buffer", IpmiNetworkFunction.App, 0x35),
    GetBTInterfaceCapabilities("Get BT Interface Capabilities", IpmiNetworkFunction.App, 0x36),
    GetSystemGUID("Get System GUID", IpmiNetworkFunction.App, 0x37),
    SetSystemInfoParameters("Set System Info Parameters", IpmiNetworkFunction.App, 0x58),
    GetSystemInfoParameters("Get System Info Parameters", IpmiNetworkFunction.App, 0x59),
    GetChannelAuthenticationCapabilities("Get Channel Authentication Capabilities", IpmiNetworkFunction.App, 0x38),
    GetSessionChallenge("Get Session Challenge", IpmiNetworkFunction.App, 0x39),
    ActivateSession("Activate Session", IpmiNetworkFunction.App, 0x3A),
    SetSessionPrivilegeLevel("Set Session Privilege Level", IpmiNetworkFunction.App, 0x3B),
    CloseSession("Close Session", IpmiNetworkFunction.App, 0x3C),
    GetSessionInfo("Get Session Info", IpmiNetworkFunction.App, 0x3D),
    unassigned("unassigned", IpmiNetworkFunction.App, 0x3E),
    GetAuthCode("Get AuthCode", IpmiNetworkFunction.App, 0x3F),
    SetChannelAccess("Set Channel Access", IpmiNetworkFunction.App, 0x40),
    GetChannelAccess("Get Channel Access", IpmiNetworkFunction.App, 0x41),
    GetChannelInfoCommand("Get Channel Info Command", IpmiNetworkFunction.App, 0x42),
    SetUserAccessCommand("Set User Access Command", IpmiNetworkFunction.App, 0x43),
    GetUserAccessCommand("Get User Access Command", IpmiNetworkFunction.App, 0x44),
    SetUserName("Set User Name", IpmiNetworkFunction.App, 0x45),
    GetUserNameCommand("Get User Name Command", IpmiNetworkFunction.App, 0x46),
    SetUserPasswordCommand("Set User Password Command", IpmiNetworkFunction.App, 0x47),
    ActivatePayload("Activate Payload", IpmiNetworkFunction.App, 0x48),
    DeactivatePayload("Deactivate Payload", IpmiNetworkFunction.App, 0x49),
    GetPayloadActivationStatus("Get Payload Activation Status", IpmiNetworkFunction.App, 0x4A),
    GetPayloadInstanceInfo("Get Payload Instance Info", IpmiNetworkFunction.App, 0x4B),
    SetUserPayloadAccess("Set User Payload Access", IpmiNetworkFunction.App, 0x4C),
    GetUserPayloadAccess("Get User Payload Access", IpmiNetworkFunction.App, 0x4D),
    GetChannelPayloadSupport("Get Channel Payload Support", IpmiNetworkFunction.App, 0x4E),
    GetChannelPayloadVersion("Get Channel Payload Version", IpmiNetworkFunction.App, 0x4F),
    GetChannelOEMPayloadInfo("Get Channel OEM Payload Info", IpmiNetworkFunction.App, 0x50),
    // unassigned("unassigned", IpmiNetworkFunction.App, 0x51),
    MasterWriteRead("Master Write-Read", IpmiNetworkFunction.App, 0x52),
    // unassigned("unassigned", IpmiNetworkFunction.App, 0x53),
    GetChannelCipherSuites("Get Channel Cipher Suites", IpmiNetworkFunction.App, 0x54),
    SuspendResumePayloadEncryption("Suspend/Resume Payload Encryption", IpmiNetworkFunction.App, 0x55),
    SetChannelSecurityKeys("Set Channel Security Keys", IpmiNetworkFunction.App, 0x56),
    GetSystemInterfaceCapabilities("Get System Interface Capabilities", IpmiNetworkFunction.App, 0x57),
    // unassigned("unassigned", IpmiNetworkFunction.App, 0x58h-5F),
    // FirmwareFirewallConfiguration("Firmware Firewall Configuration (see IPM Device Commands, above)", IpmiNetworkFunction.App, 0x60h-64),
    // Chassis Device Commands
    GetChassisCapabilities("Get Chassis Capabilities", IpmiNetworkFunction.Chassis, 0x00),
    GetChassisStatus("Get Chassis Status", IpmiNetworkFunction.Chassis, 0x01),
    ChassisControl("Chassis Control", IpmiNetworkFunction.Chassis, 0x02),
    ChassisReset("Chassis Reset", IpmiNetworkFunction.Chassis, 0x03),
    ChassisIdentify("Chassis Identify", IpmiNetworkFunction.Chassis, 0x04),
    SetFrontPanelButtonEnables("Set Front Panel Button Enables", IpmiNetworkFunction.Chassis, 0x0A),
    SetChassisCapabilities("Set Chassis Capabilities", IpmiNetworkFunction.Chassis, 0x05),
    SetPowerRestorePolicy("Set Power Restore Policy", IpmiNetworkFunction.Chassis, 0x06),
    SetPowerCycleInterval("Set Power Cycle Interval", IpmiNetworkFunction.Chassis, 0x0B),
    GetSystemRestartCause("Get System Restart Cause", IpmiNetworkFunction.Chassis, 0x07),
    SetSystemBootOptions("Set System Boot Options", IpmiNetworkFunction.Chassis, 0x08),
    GetSystemBootOptions("Get System Boot Options", IpmiNetworkFunction.Chassis, 0x09),
    // unassigned("unassigned", IpmiNetworkFunction.Chassis, 0x0Ch-0E),
    GetPOHCounter("Get POH Counter", IpmiNetworkFunction.Chassis, 0x0F),
    // Event Commands
    SetEventReceiver("Set Event Receiver", IpmiNetworkFunction.Sensor, 0x00),
    GetEventReceiver("Get Event Receiver", IpmiNetworkFunction.Sensor, 0x01),
    PlatformEvent("Platform Event (a.k.a. 'Event Message')", IpmiNetworkFunction.Sensor, 0x02),
    // unassigned("unassigned", IpmiNetworkFunction.Sensor, 0x03h-0F),
    // PEF and Alerting Commands
    GetPEFCapabilities("Get PEF Capabilities", IpmiNetworkFunction.Sensor, 0x10),
    ArmPEFPostponeTimer("Arm PEF Postpone Timer", IpmiNetworkFunction.Sensor, 0x11),
    SetPEFConfigurationParameters("Set PEF Configuration Parameters", IpmiNetworkFunction.Sensor, 0x12),
    GetPEFConfigurationParameters("Get PEF Configuration Parameters", IpmiNetworkFunction.Sensor, 0x13),
    SetLastProcessedEventID("Set Last Processed Event ID", IpmiNetworkFunction.Sensor, 0x14),
    GetLastProcessedEventID("Get Last Processed Event ID", IpmiNetworkFunction.Sensor, 0x15),
    AlertImmediate("Alert Immediate", IpmiNetworkFunction.Sensor, 0x16),
    PETAcknowledge("PET Acknowledge", IpmiNetworkFunction.Sensor, 0x17),
    // Sensor Device Commands
    GetDeviceSDRInfo("Get Device SDR Info", IpmiNetworkFunction.Sensor, 0x20),
    GetDeviceSDR("Get Device SDR", IpmiNetworkFunction.Sensor, 0x21),
    ReserveDeviceSDRRepository("Reserve Device SDR Repository", IpmiNetworkFunction.Sensor, 0x22),
    GetSensorReadingFactors("Get Sensor Reading Factors", IpmiNetworkFunction.Sensor, 0x23),
    SetSensorHysteresis("Set Sensor Hysteresis", IpmiNetworkFunction.Sensor, 0x24),
    GetSensorHysteresis("Get Sensor Hysteresis", IpmiNetworkFunction.Sensor, 0x25),
    SetSensorThreshold("Set Sensor Threshold", IpmiNetworkFunction.Sensor, 0x26),
    GetSensorThreshold("Get Sensor Threshold", IpmiNetworkFunction.Sensor, 0x27),
    SetSensorEventEnable("Set Sensor Event Enable", IpmiNetworkFunction.Sensor, 0x28),
    GetSensorEventEnable("Get Sensor Event Enable", IpmiNetworkFunction.Sensor, 0x29),
    ReArmSensorEvents("Re-arm Sensor Events", IpmiNetworkFunction.Sensor, 0x2A),
    GetSensorEventStatus("Get Sensor Event Status", IpmiNetworkFunction.Sensor, 0x2B),
    GetSensorReading("Get Sensor Reading", IpmiNetworkFunction.Sensor, 0x2D),
    SetSensorType("Set Sensor Type", IpmiNetworkFunction.Sensor, 0x2E),
    GetSensorType("Get Sensor Type", IpmiNetworkFunction.Sensor, 0x2F),
    SetSensorReadingAndEventStatus("Set Sensor Reading And Event Status", IpmiNetworkFunction.Sensor, 0x30),
    // FRU Device Commands
    GetFRUInventoryAreaInfo("Get FRU Inventory Area Info", IpmiNetworkFunction.Storage, 0x10),
    ReadFRUData("Read FRU Data", IpmiNetworkFunction.Storage, 0x11),
    WriteFRUData("Write FRU Data", IpmiNetworkFunction.Storage, 0x12),
    // SDR Device Commands
    GetSDRRepositoryInfo("Get SDR Repository Info", IpmiNetworkFunction.Storage, 0x20),
    GetSDRRepositoryAllocationInfo("Get SDR Repository Allocation Info", IpmiNetworkFunction.Storage, 0x21),
    ReserveSDRRepository("Reserve SDR Repository", IpmiNetworkFunction.Storage, 0x22),
    GetSDR("Get SDR", IpmiNetworkFunction.Storage, 0x23),
    AddSDR("Add SDR", IpmiNetworkFunction.Storage, 0x24),
    PartialAddSDR("Partial Add SDR", IpmiNetworkFunction.Storage, 0x25),
    DeleteSDR("Delete SDR", IpmiNetworkFunction.Storage, 0x26),
    ClearSDRRepository("Clear SDR Repository", IpmiNetworkFunction.Storage, 0x27),
    GetSDRRepositoryTime("Get SDR Repository Time", IpmiNetworkFunction.Storage, 0x28),
    SetSDRRepositoryTime("Set SDR Repository Time", IpmiNetworkFunction.Storage, 0x29),
    EnterSDRRepositoryUpdateMode("Enter SDR Repository Update Mode", IpmiNetworkFunction.Storage, 0x2A),
    ExitSDRRepositoryUpdateMode("Exit SDR Repository Update Mode", IpmiNetworkFunction.Storage, 0x2B),
    RunInitializationAgent("Run Initialization Agent", IpmiNetworkFunction.Storage, 0x2C),
    // SEL Device Commands
    GetSELInfo("Get SEL Info", IpmiNetworkFunction.Storage, 0x40),
    GetSELAllocationInfo("Get SEL Allocation Info", IpmiNetworkFunction.Storage, 0x41),
    ReserveSEL("Reserve SEL", IpmiNetworkFunction.Storage, 0x42),
    GetSELEntry("Get SEL Entry", IpmiNetworkFunction.Storage, 0x43),
    AddSELEntry("Add SEL Entry", IpmiNetworkFunction.Storage, 0x44),
    PartialAddSELEntry("Partial Add SEL Entry", IpmiNetworkFunction.Storage, 0x45),
    DeleteSELEntry("Delete SEL Entry", IpmiNetworkFunction.Storage, 0x46),
    ClearSEL("Clear SEL", IpmiNetworkFunction.Storage, 0x47),
    GetSELTime("Get SEL Time", IpmiNetworkFunction.Storage, 0x48),
    SetSELTime("Set SEL Time", IpmiNetworkFunction.Storage, 0x49),
    GetAuxiliaryLogStatus("Get Auxiliary Log Status", IpmiNetworkFunction.Storage, 0x5A),
    SetAuxiliaryLogStatus("Set Auxiliary Log Status", IpmiNetworkFunction.Storage, 0x5B),
    GetSELTimeUTCOffset("Get SEL Time UTC Offset", IpmiNetworkFunction.Storage, 0x5C),
    SetSELTimeUTCOffset("Set SEL Time UTC Offset", IpmiNetworkFunction.Storage, 0x5D),
    // LAN Device Commands
    SetLANConfigurationParameters("Set LAN Configuration Parameters", IpmiNetworkFunction.Transport, 0x01),
    GetLANConfigurationParameters("Get LAN Configuration Parameters", IpmiNetworkFunction.Transport, 0x02),
    SuspendBMCARPs("Suspend BMC ARPs", IpmiNetworkFunction.Transport, 0x03),
    GetIP_UDP_RMCPStatistics("Get IP/UDP/RMCP Statistics", IpmiNetworkFunction.Transport, 0x04),
    // Serial/Modem Device Commands
    SetSerialModemConfiguration("Set Serial/Modem Configuration", IpmiNetworkFunction.Transport, 0x10),
    GetSerialModemConfiguration("Get Serial/Modem Configuration", IpmiNetworkFunction.Transport, 0x11),
    SetSerialModemMux("Set Serial/Modem Mux", IpmiNetworkFunction.Transport, 0x12),
    GetTAPResponseCodes("Get TAP Response Codes", IpmiNetworkFunction.Transport, 0x13),
    SetPPPUDPProxyTransmitData("Set PPP UDP Proxy Transmit Data", IpmiNetworkFunction.Transport, 0x14),
    GetPPPUDPProxyTransmitData("Get PPP UDP Proxy Transmit Data", IpmiNetworkFunction.Transport, 0x15),
    SendPPPUDPProxyPacket("Send PPP UDP Proxy Packet", IpmiNetworkFunction.Transport, 0x16),
    GetPPPUDPProxyReceiveData("Get PPP UDP Proxy Receive Data", IpmiNetworkFunction.Transport, 0x17),
    SerialModemConnectionActive("Serial/Modem Connection Active", IpmiNetworkFunction.Transport, 0x18),
    Callback("Callback", IpmiNetworkFunction.Transport, 0x19),
    SetUserCallbackOptions("Set User Callback Options", IpmiNetworkFunction.Transport, 0x1A),
    GetUserCallbackOptions("Get User Callback Options", IpmiNetworkFunction.Transport, 0x1B),
    SetSerialRoutingMux("Set Serial Routing Mux", IpmiNetworkFunction.Transport, 0x1C),
    SOLActivating("SOL Activating", IpmiNetworkFunction.Transport, 0x20),
    SetSOLConfigurationParameters("Set SOL Configuration Parameters", IpmiNetworkFunction.Transport, 0x21),
    GetSOLConfigurationParameters("Get SOL Configuration Parameters", IpmiNetworkFunction.Transport, 0x22),
    // Command Forwarding Commands
    ForwardedCommand("Forwarded Command", IpmiNetworkFunction.Transport, 0x30),
    SetForwardedCommands("Set Forwarded Commands", IpmiNetworkFunction.Transport, 0x31),
    GetForwardedCommands("Get Forwarded Commands", IpmiNetworkFunction.Transport, 0x32),
    EnableForwardedCommands("Enable Forwarded Commands", IpmiNetworkFunction.Transport, 0x33),
    // Bridge Management Commands (ICMB)
    GetBridgeState("Get Bridge State", IpmiNetworkFunction.Bridge, 0x00),
    SetBridgeState("Set Bridge State", IpmiNetworkFunction.Bridge, 0x01),
    GetICMBAddress("Get ICMB Address", IpmiNetworkFunction.Bridge, 0x02),
    SetICMBAddress("Set ICMB Address", IpmiNetworkFunction.Bridge, 0x03),
    SetBridgeProxyAddress("Set Bridge ProxyAddress", IpmiNetworkFunction.Bridge, 0x04),
    GetBridgeStatistics("Get Bridge Statistics", IpmiNetworkFunction.Bridge, 0x05),
    GetICMBCapabilities("Get ICMB Capabilities", IpmiNetworkFunction.Bridge, 0x06),
    ClearBridgeStatistics("Clear Bridge Statistics", IpmiNetworkFunction.Bridge, 0x08),
    GetBridgeProxyAddress("Get Bridge Proxy Address", IpmiNetworkFunction.Bridge, 0x09),
    GetICMBConnectorInfo("Get ICMB Connector Info", IpmiNetworkFunction.Bridge, 0x0A),
    GetICMBConnectionID("Get ICMB Connection ID", IpmiNetworkFunction.Bridge, 0x0B),
    SendICMBConnectionID("Send ICMB Connection ID", IpmiNetworkFunction.Bridge, 0x0C),
    // Discovery Commands (ICMB)
    PrepareForDiscovery("PrepareForDiscovery", IpmiNetworkFunction.Bridge, 0x10),
    GetAddresses("GetAddresses", IpmiNetworkFunction.Bridge, 0x11),
    SetDiscovered("SetDiscovered", IpmiNetworkFunction.Bridge, 0x12),
    GetChassisDeviceId("GetChassisDeviceId", IpmiNetworkFunction.Bridge, 0x13),
    SetChassisDeviceId("SetChassisDeviceId", IpmiNetworkFunction.Bridge, 0x14),
    // Bridging Commands (ICMB)
    BridgeRequest("BridgeRequest", IpmiNetworkFunction.Bridge, 0x20),
    BridgeMessage("BridgeMessage", IpmiNetworkFunction.Bridge, 0x21),
    // Event Commands (ICMB)
    GetEventCount("GetEventCount", IpmiNetworkFunction.Bridge, 0x30),
    SetEventDestination("SetEventDestination", IpmiNetworkFunction.Bridge, 0x31),
    SetEventReceptionState("SetEventReceptionState", IpmiNetworkFunction.Bridge, 0x32),
    SendICMBEventMessage("SendICMBEventMessage", IpmiNetworkFunction.Bridge, 0x33),
    GetEventDestination("GetEventDestination (optional)", IpmiNetworkFunction.Bridge, 0x34),
    GetEventReceptionState("GetEventReceptionState (optional)", IpmiNetworkFunction.Bridge, 0x35),
    // OEM Commands for Bridge NetFn
    // OEMCommands("OEM Commands", IpmiNetworkFunction.Bridge, 0xC0h-FE),
    // Other Bridge Commands
    ErrorReport("Error Report (optional)", IpmiNetworkFunction.Bridge, 0xFF);
    private final String name;
    private final IpmiNetworkFunction networkFunction;
    private final byte code;

    private IpmiCommand(@Nonnull String name, @Nonnull IpmiNetworkFunction networkFunction, @Nonnegative int code) {
        this.name = name;
        this.networkFunction = networkFunction;
        this.code = UnsignedBytes.checkedCast(code);
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public IpmiNetworkFunction getNetworkFunction() {
        return networkFunction;
    }

    @Override
    public byte getCode() {
        return code;
    }
}
