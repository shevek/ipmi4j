/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.session;

import javax.annotation.CheckForNull;

/**
 *
 * @author shevek
 */
public interface IpmiSessionContext {

    @CheckForNull
    public IpmiSession getIpmiSession(int sessionId);
}
