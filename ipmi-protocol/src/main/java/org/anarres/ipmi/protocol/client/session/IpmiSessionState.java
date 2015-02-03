/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.session;

/**
 *
 * @author shevek
 */
public enum IpmiSessionState {

    UNKNOWN,
    AUTHCAP,
    OPENSESSION,
    RAKP1,
    RAKP3,
    SETPRIVILEGE,
    VALID;
}
