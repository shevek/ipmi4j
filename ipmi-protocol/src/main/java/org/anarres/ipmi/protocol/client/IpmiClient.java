/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.dispatch.IpmiPayloadTransmitQueue;
import org.anarres.ipmi.protocol.client.dispatch.IpmiReceiver;
import org.anarres.ipmi.protocol.client.session.IpmiSession;
import org.anarres.ipmi.protocol.client.session.IpmiSessionManager;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;

/**
 *
 * @author shevek
 */
public interface IpmiClient extends IpmiPayloadTransmitQueue.IpmiPacketSender {

    public void queue(@Nonnull IpmiHandlerContext context, @CheckForNull IpmiSession session, @Nonnull IpmiPayload message,
            @Nonnull Class<? extends IpmiPayload> responseType, @Nonnull IpmiReceiver receiver);

    @Nonnull
    public IpmiSessionManager getSessionManager();
}
