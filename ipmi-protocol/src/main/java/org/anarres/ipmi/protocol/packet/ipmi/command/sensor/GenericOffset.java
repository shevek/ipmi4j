/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sensor;

import com.google.common.primitives.UnsignedBytes;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * [IPMI2] Section 42.1, table 42-2, pages 506-507.
 *
 * @author shevek
 */
public interface GenericOffset extends Code.DescriptiveWrapper {

    /**
     * [IPMI2] Section 42.1, table 42-2, page 506.
     */
    public static enum Threshold implements GenericOffset {

        LowerNonCriticalGoingLow(0x00, "Lower Non-critical - going low"),
        LowerNonCriticalGoingHigh(0x01, "Lower Non-critical - going high"),
        LowerCriticalGoingLow(0x02, "Lower Critical - going low"),
        LowerCriticalGoingHigh(0x03, "Lower Critical - going high"),
        LowerNonRecoverableGoingLow(0x04, "Lower Non-recoverable - going low"),
        LowerNonRecoverableGoingHigh(0x05, "Lower Non-recoverable - going high"),
        UpperNonCriticalGoingLow(0x06, "Upper Non-critical - going low"),
        UpperNonCriticalGoingHigh(0x07, "Upper Non-critical - going high"),
        UpperCriticalGoingLow(0x08, "Upper Critical - going low"),
        UpperCriticalGoingHigh(0x09, "Upper Critical - going high"),
        UpperNonRecoverableGoingLow(0x0A, "Upper Non-recoverable - going low"),
        UpperNonRecoverableGoingHigh(0x0B, "Upper Non-recoverable - going high");
        private final byte code;
        private final String description;
        /* pp */ private Threshold(@Nonnegative int code, @Nonnull String description) {
            this.code = UnsignedBytes.checkedCast(code);
            this.description = description;
        }

        @Override
        public byte getCode() {
            return code;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return Code.Utils.toString(this);
        }
    }

    /**
     * [IPMI2] Section 42.1, table 42-2, page 506.
     */
    public static enum Usage implements GenericOffset {

        TransitionToIdle(0x00, "Transition to Idle"),
        TransitionToIdleToActive(0x01, "Transition to Active"),
        TransitionToIdleToBusy(0x02, "Transition to Busy");
        private final byte code;
        private final String description;
        /* pp */ private Usage(@Nonnegative int code, @Nonnull String description) {
            this.code = UnsignedBytes.checkedCast(code);
            this.description = description;
        }

        @Override
        public byte getCode() {
            return code;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return Code.Utils.toString(this);
        }
    }

    /**
     * [IPMI2] Section 42.1, table 42-2, page 506.
     */
    public static enum Assertion implements GenericOffset {

        StateDeasserted(0x00, "State Deasserted"),
        StateAsserted(0x01, "State Asserted");
        private final byte code;
        private final String description;
        /* pp */ private Assertion(@Nonnegative int code, @Nonnull String description) {
            this.code = UnsignedBytes.checkedCast(code);
            this.description = description;
        }

        @Override
        public byte getCode() {
            return code;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return Code.Utils.toString(this);
        }
    }

    /**
     * [IPMI2] Section 42.1, table 42-2, page 506.
     */
    public static enum PredictiveFailure implements GenericOffset {

        PredictiveFailureDeasserted(0x00, "Predictive Failure Deasserted"),
        PredictiveFailureAsserted(0x01, "Predictive Failure Asserted");
        private final byte code;
        private final String description;
        /* pp */ private PredictiveFailure(@Nonnegative int code, @Nonnull String description) {
            this.code = UnsignedBytes.checkedCast(code);
            this.description = description;
        }

        @Override
        public byte getCode() {
            return code;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return Code.Utils.toString(this);
        }
    }

    /**
     * [IPMI2] Section 42.1, table 42-2, page 506.
     */
    public static enum Limit implements GenericOffset {

        LimitNotExceeded(0x00, "Limit Not Exceeded"),
        LimitExceeded(0x01, "Limit Exceeded");
        private final byte code;
        private final String description;
        /* pp */ private Limit(@Nonnegative int code, @Nonnull String description) {
            this.code = UnsignedBytes.checkedCast(code);
            this.description = description;
        }

        @Override
        public byte getCode() {
            return code;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return Code.Utils.toString(this);
        }
    }

    /**
     * [IPMI2] Section 42.1, table 42-2, page 506.
     */
    public static enum Performance implements GenericOffset {

        PerformanceMet(0x00, "Performance Met"),
        PerformanceLags(0x01, "Performance Lags");
        private final byte code;
        private final String description;
        /* pp */ private Performance(@Nonnegative int code, @Nonnull String description) {
            this.code = UnsignedBytes.checkedCast(code);
            this.description = description;
        }

        @Override
        public byte getCode() {
            return code;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return Code.Utils.toString(this);
        }
    }

    /**
     * [IPMI2] Section 42.1, table 42-2, page 507.
     */
    public static enum Severity implements GenericOffset {

        TransitionToOK(0x00, "Transition to OK"),
        TransitionToNonCriticalFromOK(0x01, "Transition to Non-Critical from OK"),
        TransitionToCriticalFromLessSevere(0x02, "Transition to Critical from less severe"),
        TransitionToNonRecoverableFromLessSevere(0x03, "Transition to Non-recoverable from less severe"),
        TransitionToNonCriticalFromMoreSevere(0x04, "Transition to Non-Critical from more severe"),
        TransitionToCriticalFromNonRecoverable(0x05, "Transition to Critical from Non-recoverable"),
        TransitionToNonRecoverable(0x06, "Transition to Non-recoverable"),
        Monitor(0x07, "Monitor"),
        Informational(0x08, "Informational");
        private final byte code;
        private final String description;
        /* pp */ private Severity(@Nonnegative int code, @Nonnull String description) {
            this.code = UnsignedBytes.checkedCast(code);
            this.description = description;
        }

        @Override
        public byte getCode() {
            return code;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return Code.Utils.toString(this);
        }
    }

    /**
     * [IPMI2] Section 42.1, table 42-2, page 507.
     */
    public static enum Presence implements GenericOffset {

        DeviceAbsent(0x00, "Device Removed / Device Absent"),
        DevicePresent(0x01, "Device Inserted / Device Present");
        private final byte code;
        private final String description;
        /* pp */ private Presence(@Nonnegative int code, @Nonnull String description) {
            this.code = UnsignedBytes.checkedCast(code);
            this.description = description;
        }

        @Override
        public byte getCode() {
            return code;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return Code.Utils.toString(this);
        }
    }

    /**
     * [IPMI2] Section 42.1, table 42-2, page 507.
     */
    public static enum Enablement implements GenericOffset {

        DeviceDisabled(0x00, "Device Disabled"),
        DeviceEnabled(0x01, "Device Enabled");
        private final byte code;
        private final String description;
        /* pp */ private Enablement(@Nonnegative int code, @Nonnull String description) {
            this.code = UnsignedBytes.checkedCast(code);
            this.description = description;
        }

        @Override
        public byte getCode() {
            return code;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return Code.Utils.toString(this);
        }
    }

    /**
     * [IPMI2] Section 42.1, table 42-2, page 507.
     */
    public static enum Availability implements GenericOffset {

        TransitionToRunning(0x00, "Transition to Running"),
        TransitionToInTest(0x01, "Transition to In Test"),
        TransitionToPowerOff(0x02, "Transition to Power Off"),
        TransitionToOnLine(0x03, "Transition to On Line"),
        TransitionToOffLine(0x04, "Transition to Off Line"),
        TransitionToOffDuty(0x05, "Transition to Off Duty"),
        TransitionToDegraded(0x06, "Transition to Degraded"),
        TransitionToPowerSave(0x07, "Transition to Power Save"),
        InstallError(0x08, "Install Error");
        private final byte code;
        private final String description;
        /* pp */ private Availability(@Nonnegative int code, @Nonnull String description) {
            this.code = UnsignedBytes.checkedCast(code);
            this.description = description;
        }

        @Override
        public byte getCode() {
            return code;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return Code.Utils.toString(this);
        }
    }

    /**
     * [IPMI2] Section 42.1, table 42-2, page 507.
     */
    public static enum Redundancy implements GenericOffset {

        /** Indicates that full redundancy has been regained. (formerly 'Redundancy Regained') */
        FullyRedundant(0x00, "Fully Redundant"),
        /** Entered any non-redundant state, including Non-redundant:Insufficient Resources. */
        RedundancyLost(0x01, "Redundancy Lost"),
        /** Redundancy still exists, but at a less than full level. For example, a system has four fans, and can tolerate the failure of two of them, and presently one has failed. */
        RedundancyDegraded(0x02, "Redundancy Degraded"),
        /** Redundancy has been lost but unit is functioning with minimum resources needed for 'normal' operation.  Entered from Redundancy Degraded or Fully Redundant. */
        NonRedundant_SufficientResourcesFromRedundant(0x03, "Non-redundant:Sufficient Resources from Redundant"),
        /** Unit has regained minimum resources needed for 'normal' operation. Entered from Non-redundant:Insufficient Resources. */
        NonRedundant_SufficientResourcesFromInsufficientResources(0x04, "Non-redundant:Sufficient Resources from Insufficient Resources"),
        /** Unit is non-redundant and has insufficient resources to maintain normal operation. */
        NonRedundant_InsufficientResources(0x05, "Non-redundant:Insufficient Resources"),
        /** Unit has lost some redundant resource(s) but is still in a redundant state. Entered by a transition from Fully Redundant condition. */
        RedundancyDegradedFromFullyRedundant(0x06, "Redundancy Degraded from Fully Redundant"),
        /** Unit has regained some resource(s) and is redundant but not fully redundant. Entered from Non-redundant:Sufficient Resources or Non-redundant:Insufficient Resources. */
        RedundancyDegradedFromNonRedundant(0x07, "Redundancy Degraded from Non-redundant");
        private final byte code;
        private final String description;
        /* pp */ private Redundancy(@Nonnegative int code, @Nonnull String description) {
            this.code = UnsignedBytes.checkedCast(code);
            this.description = description;
        }

        @Override
        public byte getCode() {
            return code;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return Code.Utils.toString(this);
        }
    }

    /**
     * [IPMI2] Section 42.1, table 42-2, page 507.
     */
    public static enum PowerState implements GenericOffset {

        D0PowerState(0x00, "D0 Power State"),
        D1PowerState(0x01, "D1 Power State"),
        D2PowerState(0x02, "D2 Power State"),
        D3PowerState(0x03, "D3 Power State");
        private final byte code;
        private final String description;
        /* pp */ private PowerState(@Nonnegative int code, @Nonnull String description) {
            this.code = UnsignedBytes.checkedCast(code);
            this.description = description;
        }

        @Override
        public byte getCode() {
            return code;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return Code.Utils.toString(this);
        }
    }
}
