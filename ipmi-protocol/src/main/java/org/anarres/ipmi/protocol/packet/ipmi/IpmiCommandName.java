/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import com.google.common.base.Throwables;
import com.google.common.primitives.UnsignedBytes;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.command.IpmiCommand;
import org.anarres.ipmi.protocol.packet.ipmi.command.messaging.GetChannelAuthenticationCapabilitiesRequest;
import org.anarres.ipmi.protocol.packet.ipmi.command.IpmiRequest;
import org.anarres.ipmi.protocol.packet.ipmi.command.IpmiResponse;
import org.anarres.ipmi.protocol.packet.ipmi.command.UnknownIpmiCommand;
import org.anarres.ipmi.protocol.packet.ipmi.command.UnknownIpmiRequest;
import org.anarres.ipmi.protocol.packet.ipmi.command.UnknownIpmiResponse;
import org.anarres.ipmi.protocol.packet.ipmi.command.chassis.GetChassisStatusRequest;
import org.anarres.ipmi.protocol.packet.ipmi.command.chassis.GetChassisStatusResponse;
import org.anarres.ipmi.protocol.packet.ipmi.command.fru.GetFRUInventoryAreaInfoRequest;
import org.anarres.ipmi.protocol.packet.ipmi.command.fru.GetFRUInventoryAreaInfoResponse;
import org.anarres.ipmi.protocol.packet.ipmi.command.fru.ReadFRUDataRequest;
import org.anarres.ipmi.protocol.packet.ipmi.command.fru.ReadFRUDataResponse;
import org.anarres.ipmi.protocol.packet.ipmi.command.global.GetDeviceIdRequest;
import org.anarres.ipmi.protocol.packet.ipmi.command.global.GetDeviceIdResponse;
import org.anarres.ipmi.protocol.packet.ipmi.command.lan.GetLANConfigurationParametersRequest;
import org.anarres.ipmi.protocol.packet.ipmi.command.lan.GetLANConfigurationParametersResponse;
import org.anarres.ipmi.protocol.packet.ipmi.command.messaging.CloseSessionRequest;
import org.anarres.ipmi.protocol.packet.ipmi.command.messaging.CloseSessionResponse;
import org.anarres.ipmi.protocol.packet.ipmi.command.messaging.GetChannelAccessRequest;
import org.anarres.ipmi.protocol.packet.ipmi.command.messaging.GetChannelAccessResponse;
import org.anarres.ipmi.protocol.packet.ipmi.command.messaging.GetChannelAuthenticationCapabilitiesResponse;
import org.anarres.ipmi.protocol.packet.ipmi.command.messaging.GetChannelCipherSuitesRequest;
import org.anarres.ipmi.protocol.packet.ipmi.command.messaging.GetChannelCipherSuitesResponse;
import org.anarres.ipmi.protocol.packet.ipmi.command.messaging.GetChannelInfoRequest;
import org.anarres.ipmi.protocol.packet.ipmi.command.messaging.GetChannelInfoResponse;
import org.anarres.ipmi.protocol.packet.ipmi.command.messaging.SetSessionPrivilegeLevelRequest;
import org.anarres.ipmi.protocol.packet.ipmi.command.messaging.SetSessionPrivilegeLevelResponse;
import org.anarres.ipmi.protocol.packet.ipmi.command.sdr.GetSDRRepositoryInfoRequest;
import org.anarres.ipmi.protocol.packet.ipmi.command.sdr.GetSDRRepositoryInfoResponse;
import org.anarres.ipmi.protocol.packet.ipmi.command.sdr.GetSDRRequest;
import org.anarres.ipmi.protocol.packet.ipmi.command.sdr.GetSDRResponse;
import org.anarres.ipmi.protocol.packet.ipmi.command.sdr.ReserveSDRRepositoryRequest;
import org.anarres.ipmi.protocol.packet.ipmi.command.sdr.ReserveSDRRepositoryResponse;
import org.anarres.ipmi.protocol.packet.ipmi.command.sel.GetSELAllocationInfoRequest;
import org.anarres.ipmi.protocol.packet.ipmi.command.sel.GetSELAllocationInfoResponse;
import org.anarres.ipmi.protocol.packet.ipmi.command.sel.GetSELInfoRequest;
import org.anarres.ipmi.protocol.packet.ipmi.command.sel.GetSELInfoResponse;
import org.anarres.ipmi.protocol.packet.ipmi.command.sensor.GetSensorReadingRequest;
import org.anarres.ipmi.protocol.packet.ipmi.command.sensor.GetSensorReadingResponse;
import org.anarres.ipmi.protocol.packet.ipmi.command.sensor.GetSensorThresholdRequest;
import org.anarres.ipmi.protocol.packet.ipmi.command.sensor.GetSensorThresholdResponse;
import org.anarres.ipmi.protocol.packet.ipmi.command.sol.GetSOLConfigurationParametersRequest;
import org.anarres.ipmi.protocol.packet.ipmi.command.sol.GetSOLConfigurationParametersResponse;
import static org.anarres.ipmi.protocol.packet.ipmi.IpmiChannelPrivilegeLevel.*;

/**
 * [IPMI2] Appendix G, pages 591-596.
 *
 * @author shevek
 */
public enum IpmiCommandName implements Code.Wrapper {
// IPM Device 'Global' Commands

    // reserved("reserved", IpmiNetworkFunction.App, 0x00),
    GetDeviceID("Get Device ID", IpmiNetworkFunction.App, 0x01, User, GetDeviceIdRequest.class, GetDeviceIdResponse.class),
    BroadcastGetDeviceID("Broadcast 'Get Device ID'", IpmiNetworkFunction.App, 0x01, null), // Local only
    ColdReset("Cold Reset", IpmiNetworkFunction.App, 0x02, Administrator),
    WarmReset("Warm Reset", IpmiNetworkFunction.App, 0x03, Administrator),
    GetSelfTestResults("Get Self Test Results", IpmiNetworkFunction.App, 0x04, User),
    ManufacturingTestOn("Manufacturing Test On", IpmiNetworkFunction.App, 0x05, Administrator),
    SetACPIPowerState("Set ACPI Power State", IpmiNetworkFunction.App, 0x06, Administrator),
    GetACPIPowerState("Get ACPI Power State", IpmiNetworkFunction.App, 0x07, User),
    GetDeviceGUID("Get Device GUID", IpmiNetworkFunction.App, 0x08, User),
    GetNetFnSupport("Get NetFn Support", IpmiNetworkFunction.App, 0x09, User),
    GetCommandSupport("Get Command Support", IpmiNetworkFunction.App, 0x0A, User),
    GetCommandSubFunctionSupport("Get Command Sub-function Support", IpmiNetworkFunction.App, 0x0B, User),
    GetConfigurableCommands("Get Configurable Commands", IpmiNetworkFunction.App, 0x0C, User),
    GetConfigurableCommandSubFunctions("Get Configurable Command Sub-functions", IpmiNetworkFunction.App, 0x0D, User),
    // unassigned("unassigned", IpmiNetworkFunction.App, 0x0Eh-0F),
    SetCommandEnables("Set Command Enables", IpmiNetworkFunction.App, 0x60, Administrator),
    GetCommandEnables("Get Command Enables", IpmiNetworkFunction.App, 0x61, User),
    SetCommandSubFunctionEnables("Set Command Sub-function Enables", IpmiNetworkFunction.App, 0x62, Administrator),
    GetCommandSubFunctionEnables("Get Command Sub-function Enables", IpmiNetworkFunction.App, 0x63, User),
    GetOEMNetFnIANASupport("Get OEM NetFn IANA Support", IpmiNetworkFunction.App, 0x64, User),
    // BMC Watchdog Timer Commands
    ResetWatchdogTimer("Reset Watchdog Timer", IpmiNetworkFunction.App, 0x22, Operator),
    SetWatchdogTimer("Set Watchdog Timer", IpmiNetworkFunction.App, 0x24, Operator),
    GetWatchdogTimer("Get Watchdog Timer", IpmiNetworkFunction.App, 0x25, User),
    // BMC Device and Messaging Commands
    SetBMCGlobalEnables("Set BMC Global Enables", IpmiNetworkFunction.App, 0x2E, null),
    GetBMCGlobalEnables("Get BMC Global Enables", IpmiNetworkFunction.App, 0x2F, User),
    ClearMessageFlags("Clear Message Flags", IpmiNetworkFunction.App, 0x30, null),
    GetMessageFlags("Get Message Flags", IpmiNetworkFunction.App, 0x31, null),
    EnableMessageChannelReceive("Enable Message Channel Receive", IpmiNetworkFunction.App, 0x32, null),
    GetMessage("Get Message", IpmiNetworkFunction.App, 0x33, null),
    SendMessage("Send Message", IpmiNetworkFunction.App, 0x34, User), // Administator for some channels
    ReadEventMessageBuffer("Read Event Message Buffer", IpmiNetworkFunction.App, 0x35, null), // System interface only
    GetBTInterfaceCapabilities("Get BT Interface Capabilities", IpmiNetworkFunction.App, 0x36, User),
    GetSystemGUID("Get System GUID", IpmiNetworkFunction.App, 0x37, Unprotected),
    SetSystemInfoParameters("Set System Info Parameters", IpmiNetworkFunction.App, 0x58, Administrator),
    GetSystemInfoParameters("Get System Info Parameters", IpmiNetworkFunction.App, 0x59, User),
    GetChannelAuthenticationCapabilities("Get Channel Authentication Capabilities", IpmiNetworkFunction.App, 0x38, Unprotected, GetChannelAuthenticationCapabilitiesRequest.class, GetChannelAuthenticationCapabilitiesResponse.class),
    GetSessionChallenge("Get Session Challenge", IpmiNetworkFunction.App, 0x39, Unprotected),
    ActivateSession("Activate Session", IpmiNetworkFunction.App, 0x3A, Unprotected),
    SetSessionPrivilegeLevel("Set Session Privilege Level", IpmiNetworkFunction.App, 0x3B, User, SetSessionPrivilegeLevelRequest.class, SetSessionPrivilegeLevelResponse.class),
    CloseSession("Close Session", IpmiNetworkFunction.App, 0x3C, IpmiChannelPrivilegeLevel.Callback, CloseSessionRequest.class, CloseSessionResponse.class),
    GetSessionInfo("Get Session Info", IpmiNetworkFunction.App, 0x3D, User),
    // unassigned("unassigned", IpmiNetworkFunction.App, 0x3E),
    GetAuthCode("Get AuthCode", IpmiNetworkFunction.App, 0x3F, Operator),
    SetChannelAccess("Set Channel Access", IpmiNetworkFunction.App, 0x40, Administrator),
    GetChannelAccess("Get Channel Access", IpmiNetworkFunction.App, 0x41, User, GetChannelAccessRequest.class, GetChannelAccessResponse.class),
    GetChannelInfoCommand("Get Channel Info Command", IpmiNetworkFunction.App, 0x42, User, GetChannelInfoRequest.class, GetChannelInfoResponse.class),
    SetUserAccessCommand("Set User Access Command", IpmiNetworkFunction.App, 0x43, Administrator),
    GetUserAccessCommand("Get User Access Command", IpmiNetworkFunction.App, 0x44, Operator),
    SetUserName("Set User Name", IpmiNetworkFunction.App, 0x45, Administrator),
    GetUserNameCommand("Get User Name Command", IpmiNetworkFunction.App, 0x46, Operator),
    SetUserPasswordCommand("Set User Password Command", IpmiNetworkFunction.App, 0x47, Administrator),
    ActivatePayload("Activate Payload", IpmiNetworkFunction.App, 0x48), // Depends on payload type.
    DeactivatePayload("Deactivate Payload", IpmiNetworkFunction.App, 0x49), // Depends on payload type.
    GetPayloadActivationStatus("Get Payload Activation Status", IpmiNetworkFunction.App, 0x4A, User),
    GetPayloadInstanceInfo("Get Payload Instance Info", IpmiNetworkFunction.App, 0x4B, User),
    SetUserPayloadAccess("Set User Payload Access", IpmiNetworkFunction.App, 0x4C, Administrator),
    GetUserPayloadAccess("Get User Payload Access", IpmiNetworkFunction.App, 0x4D, Operator),
    GetChannelPayloadSupport("Get Channel Payload Support", IpmiNetworkFunction.App, 0x4E, User),
    GetChannelPayloadVersion("Get Channel Payload Version", IpmiNetworkFunction.App, 0x4F, User),
    GetChannelOEMPayloadInfo("Get Channel OEM Payload Info", IpmiNetworkFunction.App, 0x50, User),
    // unassigned("unassigned", IpmiNetworkFunction.App, 0x51),
    MasterWriteRead("Master Write-Read", IpmiNetworkFunction.App, 0x52, Operator),
    // unassigned("unassigned", IpmiNetworkFunction.App, 0x53),
    GetChannelCipherSuites("Get Channel Cipher Suites", IpmiNetworkFunction.App, 0x54, Unprotected, GetChannelCipherSuitesRequest.class, GetChannelCipherSuitesResponse.class),
    SuspendResumePayloadEncryption("Suspend/Resume Payload Encryption", IpmiNetworkFunction.App, 0x55, User),
    SetChannelSecurityKeys("Set Channel Security Keys", IpmiNetworkFunction.App, 0x56, Administrator),
    GetSystemInterfaceCapabilities("Get System Interface Capabilities", IpmiNetworkFunction.App, 0x57, User),
    // unassigned("unassigned", IpmiNetworkFunction.App, 0x58h-5F),
    // FirmwareFirewallConfiguration("Firmware Firewall Configuration (see IPM Device Commands, above)", IpmiNetworkFunction.App, 0x60h-64),
    // Chassis Device Commands
    GetChassisCapabilities("Get Chassis Capabilities", IpmiNetworkFunction.Chassis, 0x00, User),
    GetChassisStatus("Get Chassis Status", IpmiNetworkFunction.Chassis, 0x01, User, GetChassisStatusRequest.class, GetChassisStatusResponse.class),
    ChassisControl("Chassis Control", IpmiNetworkFunction.Chassis, 0x02, Operator),
    ChassisReset("Chassis Reset", IpmiNetworkFunction.Chassis, 0x03, Operator),
    ChassisIdentify("Chassis Identify", IpmiNetworkFunction.Chassis, 0x04, Operator),
    SetFrontPanelButtonEnables("Set Front Panel Button Enables", IpmiNetworkFunction.Chassis, 0x0A, Administrator),
    SetChassisCapabilities("Set Chassis Capabilities", IpmiNetworkFunction.Chassis, 0x05, Administrator),
    SetPowerRestorePolicy("Set Power Restore Policy", IpmiNetworkFunction.Chassis, 0x06, Operator),
    SetPowerCycleInterval("Set Power Cycle Interval", IpmiNetworkFunction.Chassis, 0x0B, Administrator),
    GetSystemRestartCause("Get System Restart Cause", IpmiNetworkFunction.Chassis, 0x07, User),
    SetSystemBootOptions("Set System Boot Options", IpmiNetworkFunction.Chassis, 0x08, Operator),
    GetSystemBootOptions("Get System Boot Options", IpmiNetworkFunction.Chassis, 0x09, Operator),
    // unassigned("unassigned", IpmiNetworkFunction.Chassis, 0x0Ch-0E),
    GetPOHCounter("Get POH Counter", IpmiNetworkFunction.Chassis, 0x0F, User),
    // Event Commands
    SetEventReceiver("Set Event Receiver", IpmiNetworkFunction.Sensor, 0x00, Administrator),
    GetEventReceiver("Get Event Receiver", IpmiNetworkFunction.Sensor, 0x01, User),
    PlatformEvent("Platform Event (a.k.a. 'Event Message')", IpmiNetworkFunction.Sensor, 0x02, Operator),
    // unassigned("unassigned", IpmiNetworkFunction.Sensor, 0x03h-0F),
    // PEF and Alerting Commands
    GetPEFCapabilities("Get PEF Capabilities", IpmiNetworkFunction.Sensor, 0x10, User),
    ArmPEFPostponeTimer("Arm PEF Postpone Timer", IpmiNetworkFunction.Sensor, 0x11, Administrator),
    SetPEFConfigurationParameters("Set PEF Configuration Parameters", IpmiNetworkFunction.Sensor, 0x12, Administrator),
    GetPEFConfigurationParameters("Get PEF Configuration Parameters", IpmiNetworkFunction.Sensor, 0x13, Operator),
    SetLastProcessedEventID("Set Last Processed Event ID", IpmiNetworkFunction.Sensor, 0x14, Administrator),
    GetLastProcessedEventID("Get Last Processed Event ID", IpmiNetworkFunction.Sensor, 0x15, Administrator),
    AlertImmediate("Alert Immediate", IpmiNetworkFunction.Sensor, 0x16, Administrator),
    PETAcknowledge("PET Acknowledge", IpmiNetworkFunction.Sensor, 0x17, Unprotected),
    // Sensor Device Commands
    GetDeviceSDRInfo("Get Device SDR Info", IpmiNetworkFunction.Sensor, 0x20), // Local only
    GetDeviceSDR("Get Device SDR", IpmiNetworkFunction.Sensor, 0x21), // Local only
    ReserveDeviceSDRRepository("Reserve Device SDR Repository", IpmiNetworkFunction.Sensor, 0x22), // Local only
    GetSensorReadingFactors("Get Sensor Reading Factors", IpmiNetworkFunction.Sensor, 0x23, User),
    SetSensorHysteresis("Set Sensor Hysteresis", IpmiNetworkFunction.Sensor, 0x24, Operator),
    GetSensorHysteresis("Get Sensor Hysteresis", IpmiNetworkFunction.Sensor, 0x25, User),
    SetSensorThreshold("Set Sensor Threshold", IpmiNetworkFunction.Sensor, 0x26, Operator),
    GetSensorThreshold("Get Sensor Threshold", IpmiNetworkFunction.Sensor, 0x27, User, GetSensorThresholdRequest.class, GetSensorThresholdResponse.class),
    SetSensorEventEnable("Set Sensor Event Enable", IpmiNetworkFunction.Sensor, 0x28, Operator),
    GetSensorEventEnable("Get Sensor Event Enable", IpmiNetworkFunction.Sensor, 0x29, User),
    ReArmSensorEvents("Re-arm Sensor Events", IpmiNetworkFunction.Sensor, 0x2A, Operator),
    GetSensorEventStatus("Get Sensor Event Status", IpmiNetworkFunction.Sensor, 0x2B, User),
    GetSensorReading("Get Sensor Reading", IpmiNetworkFunction.Sensor, 0x2D, User, GetSensorReadingRequest.class, GetSensorReadingResponse.class),
    SetSensorType("Set Sensor Type", IpmiNetworkFunction.Sensor, 0x2E, Operator),
    GetSensorType("Get Sensor Type", IpmiNetworkFunction.Sensor, 0x2F, User),
    SetSensorReadingAndEventStatus("Set Sensor Reading And Event Status", IpmiNetworkFunction.Sensor, 0x30, Operator),
    // FRU Device Commands
    GetFRUInventoryAreaInfo("Get FRU Inventory Area Info", IpmiNetworkFunction.Storage, 0x10, User, GetFRUInventoryAreaInfoRequest.class, GetFRUInventoryAreaInfoResponse.class),
    ReadFRUData("Read FRU Data", IpmiNetworkFunction.Storage, 0x11, User, ReadFRUDataRequest.class, ReadFRUDataResponse.class),
    WriteFRUData("Write FRU Data", IpmiNetworkFunction.Storage, 0x12, Operator),
    // SDR Device Commands
    GetSDRRepositoryInfo("Get SDR Repository Info", IpmiNetworkFunction.Storage, 0x20, User, GetSDRRepositoryInfoRequest.class, GetSDRRepositoryInfoResponse.class),
    GetSDRRepositoryAllocationInfo("Get SDR Repository Allocation Info", IpmiNetworkFunction.Storage, 0x21, User),
    ReserveSDRRepository("Reserve SDR Repository", IpmiNetworkFunction.Storage, 0x22, User, ReserveSDRRepositoryRequest.class, ReserveSDRRepositoryResponse.class),
    GetSDR("Get SDR", IpmiNetworkFunction.Storage, 0x23, User, GetSDRRequest.class, GetSDRResponse.class),
    AddSDR("Add SDR", IpmiNetworkFunction.Storage, 0x24, Operator),
    PartialAddSDR("Partial Add SDR", IpmiNetworkFunction.Storage, 0x25, Operator),
    DeleteSDR("Delete SDR", IpmiNetworkFunction.Storage, 0x26, Operator),
    ClearSDRRepository("Clear SDR Repository", IpmiNetworkFunction.Storage, 0x27, Operator),
    GetSDRRepositoryTime("Get SDR Repository Time", IpmiNetworkFunction.Storage, 0x28, User),
    SetSDRRepositoryTime("Set SDR Repository Time", IpmiNetworkFunction.Storage, 0x29, Operator),
    EnterSDRRepositoryUpdateMode("Enter SDR Repository Update Mode", IpmiNetworkFunction.Storage, 0x2A, Operator),
    ExitSDRRepositoryUpdateMode("Exit SDR Repository Update Mode", IpmiNetworkFunction.Storage, 0x2B, Operator),
    RunInitializationAgent("Run Initialization Agent", IpmiNetworkFunction.Storage, 0x2C, Operator),
    // SEL Device Commands
    GetSELInfo("Get SEL Info", IpmiNetworkFunction.Storage, 0x40, User, GetSELInfoRequest.class, GetSELInfoResponse.class),
    GetSELAllocationInfo("Get SEL Allocation Info", IpmiNetworkFunction.Storage, 0x41, User, GetSELAllocationInfoRequest.class, GetSELAllocationInfoResponse.class),
    ReserveSEL("Reserve SEL", IpmiNetworkFunction.Storage, 0x42, User),
    GetSELEntry("Get SEL Entry", IpmiNetworkFunction.Storage, 0x43, User),
    AddSELEntry("Add SEL Entry", IpmiNetworkFunction.Storage, 0x44, Operator),
    PartialAddSELEntry("Partial Add SEL Entry", IpmiNetworkFunction.Storage, 0x45, Operator),
    DeleteSELEntry("Delete SEL Entry", IpmiNetworkFunction.Storage, 0x46, Operator),
    ClearSEL("Clear SEL", IpmiNetworkFunction.Storage, 0x47, Operator),
    GetSELTime("Get SEL Time", IpmiNetworkFunction.Storage, 0x48, User),
    SetSELTime("Set SEL Time", IpmiNetworkFunction.Storage, 0x49, Operator),
    GetAuxiliaryLogStatus("Get Auxiliary Log Status", IpmiNetworkFunction.Storage, 0x5A, User),
    SetAuxiliaryLogStatus("Set Auxiliary Log Status", IpmiNetworkFunction.Storage, 0x5B, Administrator),
    GetSELTimeUTCOffset("Get SEL Time UTC Offset", IpmiNetworkFunction.Storage, 0x5C, User),
    SetSELTimeUTCOffset("Set SEL Time UTC Offset", IpmiNetworkFunction.Storage, 0x5D, Operator),
    // LAN Device Commands
    SetLANConfigurationParameters("Set LAN Configuration Parameters", IpmiNetworkFunction.Transport, 0x01, Administrator),
    GetLANConfigurationParameters("Get LAN Configuration Parameters", IpmiNetworkFunction.Transport, 0x02, Operator, GetLANConfigurationParametersRequest.class, GetLANConfigurationParametersResponse.class),
    SuspendBMCARPs("Suspend BMC ARPs", IpmiNetworkFunction.Transport, 0x03, Administrator),
    GetIP_UDP_RMCPStatistics("Get IP/UDP/RMCP Statistics", IpmiNetworkFunction.Transport, 0x04, User),
    // Serial/Modem Device Commands
    SetSerialModemConfiguration("Set Serial/Modem Configuration", IpmiNetworkFunction.Transport, 0x10, Administrator),
    GetSerialModemConfiguration("Get Serial/Modem Configuration", IpmiNetworkFunction.Transport, 0x11, Operator),
    SetSerialModemMux("Set Serial/Modem Mux", IpmiNetworkFunction.Transport, 0x12, Operator),
    GetTAPResponseCodes("Get TAP Response Codes", IpmiNetworkFunction.Transport, 0x13, User),
    SetPPPUDPProxyTransmitData("Set PPP UDP Proxy Transmit Data", IpmiNetworkFunction.Transport, 0x14), // System only
    GetPPPUDPProxyTransmitData("Get PPP UDP Proxy Transmit Data", IpmiNetworkFunction.Transport, 0x15), // System only
    SendPPPUDPProxyPacket("Send PPP UDP Proxy Packet", IpmiNetworkFunction.Transport, 0x16), // System only
    GetPPPUDPProxyReceiveData("Get PPP UDP Proxy Receive Data", IpmiNetworkFunction.Transport, 0x17), // System only
    SerialModemConnectionActive("Serial/Modem Connection Active", IpmiNetworkFunction.Transport, 0x18, Unprotected), // Before session
    Callback("Callback", IpmiNetworkFunction.Transport, 0x19, Administrator),
    SetUserCallbackOptions("Set User Callback Options", IpmiNetworkFunction.Transport, 0x1A, Administrator),
    GetUserCallbackOptions("Get User Callback Options", IpmiNetworkFunction.Transport, 0x1B, User),
    SetSerialRoutingMux("Set Serial Routing Mux", IpmiNetworkFunction.Transport, 0x1C, Administrator),
    SOLActivating("SOL Activating", IpmiNetworkFunction.Transport, 0x20), // Weird.
    SetSOLConfigurationParameters("Set SOL Configuration Parameters", IpmiNetworkFunction.Transport, 0x21, Administrator),
    GetSOLConfigurationParameters("Get SOL Configuration Parameters", IpmiNetworkFunction.Transport, 0x22, User, GetSOLConfigurationParametersRequest.class, GetSOLConfigurationParametersResponse.class),
    // Command Forwarding Commands
    ForwardedCommand("Forwarded Command", IpmiNetworkFunction.Transport, 0x30), // Weird
    SetForwardedCommands("Set Forwarded Commands", IpmiNetworkFunction.Transport, 0x31, Administrator),
    GetForwardedCommands("Get Forwarded Commands", IpmiNetworkFunction.Transport, 0x32, User),
    EnableForwardedCommands("Enable Forwarded Commands", IpmiNetworkFunction.Transport, 0x33, Administrator),
    // Bridge Management Commands (ICMB)
    GetBridgeState("Get Bridge State", IpmiNetworkFunction.Bridge, 0x00, User),
    SetBridgeState("Set Bridge State", IpmiNetworkFunction.Bridge, 0x01, Operator),
    GetICMBAddress("Get ICMB Address", IpmiNetworkFunction.Bridge, 0x02, User),
    SetICMBAddress("Set ICMB Address", IpmiNetworkFunction.Bridge, 0x03, Operator),
    SetBridgeProxyAddress("Set Bridge ProxyAddress", IpmiNetworkFunction.Bridge, 0x04, Operator),
    GetBridgeStatistics("Get Bridge Statistics", IpmiNetworkFunction.Bridge, 0x05, User),
    GetICMBCapabilities("Get ICMB Capabilities", IpmiNetworkFunction.Bridge, 0x06, User),
    ClearBridgeStatistics("Clear Bridge Statistics", IpmiNetworkFunction.Bridge, 0x08, Operator),
    GetBridgeProxyAddress("Get Bridge Proxy Address", IpmiNetworkFunction.Bridge, 0x09, User),
    GetICMBConnectorInfo("Get ICMB Connector Info", IpmiNetworkFunction.Bridge, 0x0A, User),
    GetICMBConnectionID("Get ICMB Connection ID", IpmiNetworkFunction.Bridge, 0x0B, User),
    SendICMBConnectionID("Send ICMB Connection ID", IpmiNetworkFunction.Bridge, 0x0C, User),
    // Discovery Commands (ICMB)
    PrepareForDiscovery("PrepareForDiscovery", IpmiNetworkFunction.Bridge, 0x10, Operator),
    GetAddresses("GetAddresses", IpmiNetworkFunction.Bridge, 0x11, User),
    SetDiscovered("SetDiscovered", IpmiNetworkFunction.Bridge, 0x12, Operator),
    GetChassisDeviceId("GetChassisDeviceId", IpmiNetworkFunction.Bridge, 0x13, User),
    SetChassisDeviceId("SetChassisDeviceId", IpmiNetworkFunction.Bridge, 0x14, Operator),
    // Bridging Commands (ICMB)
    BridgeRequest("BridgeRequest", IpmiNetworkFunction.Bridge, 0x20, Operator),
    BridgeMessage("BridgeMessage", IpmiNetworkFunction.Bridge, 0x21, Operator),
    // Event Commands (ICMB)
    GetEventCount("GetEventCount", IpmiNetworkFunction.Bridge, 0x30, User),
    SetEventDestination("SetEventDestination", IpmiNetworkFunction.Bridge, 0x31, Operator),
    SetEventReceptionState("SetEventReceptionState", IpmiNetworkFunction.Bridge, 0x32, Operator),
    SendICMBEventMessage("SendICMBEventMessage", IpmiNetworkFunction.Bridge, 0x33, Operator),
    GetEventDestination("GetEventDestination (optional)", IpmiNetworkFunction.Bridge, 0x34, User),
    GetEventReceptionState("GetEventReceptionState (optional)", IpmiNetworkFunction.Bridge, 0x35, User),
    // OEM commands for PICMG
    /** [IPMI2] Section 5.1, table 5-1, page 41, "Group Extension" */
    PICMGExtension("PICMG Non-IPMI Command", IpmiNetworkFunction.GroupExtension, 0x00, Unprotected),
    DMTFExtension("DMTF Non-IPMI Command", IpmiNetworkFunction.GroupExtension, 0x01, Unprotected),
    SSIForumExtension("SSI Forum Non-IPMI Command", IpmiNetworkFunction.GroupExtension, 0x02, Unprotected),
    VITAStandardsExtension("VITA Standards Organization Non-IPMI Command", IpmiNetworkFunction.GroupExtension, 0x03, Unprotected),
    DCMIExtension("DCMI Specifications Non-IPMI Command", IpmiNetworkFunction.GroupExtension, 0xDC, Unprotected),
    // OEM Commands for Bridge NetFn
    // OEMCommands("OEM Commands", IpmiNetworkFunction.Bridge, 0xC0h-FE),
    // Other Bridge Commands
    ErrorReport("Error Report (optional)", IpmiNetworkFunction.Bridge, 0xFF, User);
    private final String name;
    private final IpmiNetworkFunction networkFunction;
    private final byte code;
    private final IpmiChannelPrivilegeLevel privilegeLevel;
    private final Class<? extends IpmiRequest> requestType;
    private final Class<? extends IpmiResponse> responseType;

    private IpmiCommandName(@Nonnull String name, @Nonnull IpmiNetworkFunction networkFunction, @Nonnegative int code, @CheckForNull IpmiChannelPrivilegeLevel privilegeLevel,
            @CheckForNull Class<? extends IpmiRequest> requestType, @CheckForNull Class<? extends IpmiResponse> responseType) {
        this.name = name;
        this.networkFunction = networkFunction;
        this.code = UnsignedBytes.checkedCast(code);
        this.privilegeLevel = privilegeLevel;
        this.requestType = requestType;
        this.responseType = responseType;
    }

    private IpmiCommandName(@Nonnull String name, @Nonnull IpmiNetworkFunction networkFunction, @Nonnegative int code, @CheckForNull IpmiChannelPrivilegeLevel privilegeLevel) {
        this(name, networkFunction, code, privilegeLevel, null, null);
    }

    @Deprecated
    private IpmiCommandName(@Nonnull String name, @Nonnull IpmiNetworkFunction networkFunction, @Nonnegative int code) {
        this(name, networkFunction, code, null);
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

    @CheckForNull
    public IpmiChannelPrivilegeLevel getPrivilegeLevel() {
        return privilegeLevel;
    }

    // TODO: Packet data factory.
    @Override
    public String toString() {
        return name() + "(" + getNetworkFunction().name() + ".0x" + UnsignedBytes.toString(getCode(), 16) + ": " + getName() + " [" + getPrivilegeLevel() + "])";
    }

    /**
     * @see Code#fromByte(Class, byte)
     */
    @Nonnull
    public static IpmiCommandName fromByte(@Nonnull IpmiNetworkFunction networkFunction, byte code) {
        for (IpmiCommandName value : values())
            if (networkFunction.equals(value.getNetworkFunction()))
                if (value.getCode() == code)
                    return value;
        throw new IllegalArgumentException("Unknown IpmiCommand code for netfn " + networkFunction + " 0x" + Integer.toHexString(code));
    }

    @Nonnull
    private <T extends IpmiCommand> T newInstance(@Nonnull Class<T> type) {
        try {
            return type.newInstance();
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    @Nonnull
    public IpmiCommand newRequestMessage() {
        if (requestType == null)
            return new UnknownIpmiRequest(this);
        return newInstance(requestType);
    }

    @Nonnull
    public IpmiCommand newResponseMessage() {
        if (requestType == null)
            return new UnknownIpmiResponse(this);
        return newInstance(responseType);
    }
}
