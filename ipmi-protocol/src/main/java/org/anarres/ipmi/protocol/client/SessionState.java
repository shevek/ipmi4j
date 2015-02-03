/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client;

/**
 *
 * @author shevek
 */
public enum SessionState {

    UNKNOWN,
    AUTHCAP,
    OPENSESSION,
    RAKP1,
    RAKP3,
    SETPRIVILEGE,
    VALID;
}
