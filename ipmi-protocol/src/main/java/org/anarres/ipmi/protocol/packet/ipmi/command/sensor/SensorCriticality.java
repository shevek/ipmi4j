/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sensor;

/**
 * The criticality of a sensor threshold.
 *
 * @see SensorThreshold
 * @see SensorBoundary
 * @author shevek
 */
public enum SensorCriticality {

    NonCritical, Critical, NonRecoverable
}
