/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.session;

import javax.annotation.CheckForNull;

/**
 *
 * @author shevek
 */
public interface IpmiContext {

    @CheckForNull
    public IpmiSession getIpmiSession(int sessionId);
}
