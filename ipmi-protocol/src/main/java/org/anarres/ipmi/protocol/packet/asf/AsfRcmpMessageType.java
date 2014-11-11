/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

// Page 22
public enum AsfRcmpMessageType {

    Reset(0x10),
    PowerUp(0x11),
    UnconditionalPowerDown(0x12),
    PowerCycleReset(0x13),
    PresencePong(0x40),
    CapabilitiesResponse(0x41),
    SystemStateResponse(0x42),
    OpenSessionResponse(0x43),
    CloseSessionResponse(0x44),
    PresencePing(0x80),
    CapabilitiesRequesT(0x81),
    SystemStateRequest(0x82),
    OpenSessionRequest(0x83),
    CloseSessionRequest(0x84),
    RAKPMessage1(0xC0),
    RAKPMessage2(0xC1),
    RAKPMessage3(0xC2);
    final int value;

    private AsfRcmpMessageType(int value) {
        this.value = value;
    }
}
