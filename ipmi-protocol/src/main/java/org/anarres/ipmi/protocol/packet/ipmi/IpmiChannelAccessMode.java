/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

/**
 * [IPMI2] Section 6.6, table 6-4, page 50.
 *
 * @author shevek
 */
public enum IpmiChannelAccessMode {

    PreBootOnly, AlwaysAvailable, Shared, Disabled;
}
