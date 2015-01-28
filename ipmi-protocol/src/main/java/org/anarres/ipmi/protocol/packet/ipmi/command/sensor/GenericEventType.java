/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sensor;

import com.google.common.primitives.UnsignedBytes;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * [IPMI2] Section 42.1, table 42-2, pages 506-507.
 *
 * @author shevek
 */
public enum GenericEventType implements Code.DescriptiveWrapper {

    Threshold(0x01, GenericEventTypeCategory.Threshold, "Threshold", GenericOffset.Threshold.class),
    Usage(0x02, GenericEventTypeCategory.Discrete, "Discrete", GenericOffset.Usage.class),
    Assertion(0x03, GenericEventTypeCategory.DigitalDiscrete, "Discrete", GenericOffset.Assertion.class),
    PredictiveFailure(0x04, GenericEventTypeCategory.DigitalDiscrete, "Discrete", GenericOffset.PredictiveFailure.class),
    Limit(0x05, GenericEventTypeCategory.DigitalDiscrete, "Discrete", GenericOffset.Limit.class),
    Performance(0x06, GenericEventTypeCategory.DigitalDiscrete, "Discrete", GenericOffset.Performance.class),
    Severity(0x07, GenericEventTypeCategory.Discrete, "Discrete", GenericOffset.Severity.class),
    Presence(0x08, GenericEventTypeCategory.DigitalDiscrete, "Discrete", GenericOffset.Presence.class),
    Enablement(0x09, GenericEventTypeCategory.DigitalDiscrete, "Discrete", GenericOffset.Enablement.class),
    Availability(0x0A, GenericEventTypeCategory.Discrete, "Discrete", GenericOffset.Availability.class),
    Redundancy(0x0B, GenericEventTypeCategory.Discrete, "Discrete", GenericOffset.Redundancy.class),
    PowerState(0x0C, GenericEventTypeCategory.Discrete, "Discrete", GenericOffset.PowerState.class),
    SensorSpecific(0x6F, GenericEventTypeCategory.SensorSpecific, "Discrete"),
    OEM_70(0x70, GenericEventTypeCategory.OEM, "OEM Reading Type 0x70"),
    OEM_71(0x71, GenericEventTypeCategory.OEM, "OEM Reading Type 0x71"),
    OEM_72(0x72, GenericEventTypeCategory.OEM, "OEM Reading Type 0x72"),
    OEM_73(0x73, GenericEventTypeCategory.OEM, "OEM Reading Type 0x73"),
    OEM_74(0x74, GenericEventTypeCategory.OEM, "OEM Reading Type 0x74"),
    OEM_75(0x75, GenericEventTypeCategory.OEM, "OEM Reading Type 0x75"),
    OEM_76(0x76, GenericEventTypeCategory.OEM, "OEM Reading Type 0x76"),
    OEM_77(0x77, GenericEventTypeCategory.OEM, "OEM Reading Type 0x77"),
    OEM_78(0x78, GenericEventTypeCategory.OEM, "OEM Reading Type 0x78"),
    OEM_79(0x79, GenericEventTypeCategory.OEM, "OEM Reading Type 0x79"),
    OEM_7A(0x7A, GenericEventTypeCategory.OEM, "OEM Reading Type 0x7A"),
    OEM_7B(0x7B, GenericEventTypeCategory.OEM, "OEM Reading Type 0x7B"),
    OEM_7C(0x7C, GenericEventTypeCategory.OEM, "OEM Reading Type 0x7C"),
    OEM_7D(0x7D, GenericEventTypeCategory.OEM, "OEM Reading Type 0x7D"),
    OEM_7E(0x7E, GenericEventTypeCategory.OEM, "OEM Reading Type 0x7E"),
    OEM_7F(0x7F, GenericEventTypeCategory.OEM, "OEM Reading Type 0x7F");
    private final byte code;
    private final GenericEventTypeCategory category;
    private final String description;
    private final Class<? extends GenericOffset> genericOffsetType;
    /* pp */ private GenericEventType(
            @Nonnegative int code,
            @Nonnull GenericEventTypeCategory category,
            @Nonnull String description,
            @CheckForNull Class<? extends GenericOffset> genericOffsetType) {
        this.category = category;
        this.code = UnsignedBytes.checkedCast(code);
        this.description = description;
        this.genericOffsetType = genericOffsetType;
    }
    /* pp */ private GenericEventType(
            @Nonnegative int code,
            @Nonnull GenericEventTypeCategory category,
            @Nonnull String description) {
        this(code, category, description, null);
    }

    @Nonnull
    public GenericEventTypeCategory getCategory() {
        return category;
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
        return "0x" + UnsignedBytes.toString(getCode(), 16) + ": " + getDescription();
    }
}
