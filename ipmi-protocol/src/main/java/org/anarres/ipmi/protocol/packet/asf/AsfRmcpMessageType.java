/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import com.google.common.primitives.UnsignedBytes;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Code;

/** Page 22. */
public enum AsfRmcpMessageType implements Code.Wrapper {

    Reset(0x10) {
        @Override
        public AbstractAsfData newPacketData() {
            return new AsfResetData();
        }
    },
    PowerUp(0x11) {
        @Override
        public AbstractAsfData newPacketData() {
            return new AsfPowerUpData();
        }
    },
    UnconditionalPowerDown(0x12) {
        @Override
        public AbstractAsfData newPacketData() {
            return new AsfUnconditionalPowerDownData();
        }
    },
    PowerCycleReset(0x13) {
        @Override
        public AbstractAsfData newPacketData() {
            return new AsfPowerCycleResetData();
        }
    },
    PresencePong(0x40) {
        @Override
        public AbstractAsfData newPacketData() {
            return new AsfPresencePongData();
        }
    },
    CapabilitiesResponse(0x41) {
        @Override
        public AbstractAsfData newPacketData() {
            return new AsfCapabilitiesResponseData();
        }
    },
    SystemStateResponse(0x42) {
        @Override
        public AbstractAsfData newPacketData() {
            return new AsfSystemStateResponseData();
        }
    },
    OpenSessionResponse(0x43) {
        @Override
        public AbstractAsfData newPacketData() {
            return new AsfOpenSessionResponseData();
        }
    },
    CloseSessionResponse(0x44) {
        @Override
        public AbstractAsfData newPacketData() {
            return new AsfCloseSessionResponseData();
        }
    },
    PresencePing(0x80) {
        @Override
        public AbstractAsfData newPacketData() {
            return new AsfPresencePingData();
        }
    },
    CapabilitiesRequest(0x81) {
        @Override
        public AbstractAsfData newPacketData() {
            return new AsfCapabilitiesRequestData();
        }
    },
    SystemStateRequest(0x82) {
        @Override
        public AbstractAsfData newPacketData() {
            return new AsfSystemStateRequestData();
        }
    },
    OpenSessionRequest(0x83) {
        @Override
        public AbstractAsfData newPacketData() {
            return new AsfOpenSessionRequestData();
        }
    },
    CloseSessionRequest(0x84) {
        @Override
        public AbstractAsfData newPacketData() {
            return new AsfCloseSessionRequestData();
        }
    },
    RAKPMessage1(0xC0) {
        @Override
        public AbstractAsfData newPacketData() {
            return new AsfRAKPMessage1Data();
        }
    },
    RAKPMessage2(0xC1) {
        @Override
        public AbstractAsfData newPacketData() {
            return new AsfRAKPMessage2Data();
        }
    },
    RAKPMessage3(0xC2) {
        @Override
        public AbstractAsfData newPacketData() {
            return new AsfRAKPMessage3Data();
        }
    };
    final byte code;

    private AsfRmcpMessageType(@Nonnegative int code) {
        this.code = UnsignedBytes.checkedCast(code);
    }

    @Override
    public byte getCode() {
        return code;
    }

    @Nonnull
    public abstract AbstractAsfData newPacketData();
}
