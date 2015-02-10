/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client;

import java.security.SecureRandom;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.session.IpmiSessionManager;
import org.anarres.ipmi.protocol.packet.rmcp.Packet;

/**
 *
 * @author shevek
 */
public interface IpmiClient {

    @Nonnull
    public IpmiSessionManager getSessionManager();

    public void send(@Nonnull Packet packet);
}
