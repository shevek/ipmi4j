/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sensor;

/**
 * [IPMI2] Section 42.1, table 42-1, page 506.
 *
 * @author shevek
 */
public enum GenericEventTypeCategory {

    Unspecified, Threshold, Discrete, DigitalDiscrete, SensorSpecific, OEM;
}
