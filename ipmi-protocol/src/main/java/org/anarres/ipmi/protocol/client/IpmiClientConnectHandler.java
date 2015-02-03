/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client;

import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiSession;

/**
 *
 * @author shevek
 */
public interface IpmiClientConnectHandler {

    public void handleSession(@Nonnull IpmiSession session);
}
