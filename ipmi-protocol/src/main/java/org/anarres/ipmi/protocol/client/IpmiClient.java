/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client;

import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.dispatch.IpmiPayloadTransmitQueue;
import org.anarres.ipmi.protocol.client.session.IpmiSessionManager;

/**
 *
 * @author shevek
 */
public interface IpmiClient extends IpmiPayloadTransmitQueue.Sender {

    @Nonnull
    public IpmiSessionManager getSessionManager();
}
