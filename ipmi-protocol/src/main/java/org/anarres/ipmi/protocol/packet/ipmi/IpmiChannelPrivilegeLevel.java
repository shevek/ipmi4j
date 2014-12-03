/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

/**
 *
 * @author shevek
 */
public enum IpmiChannelPrivilegeLevel {

    Unprotected,
    Callback, User, Operator, Administator;
}
