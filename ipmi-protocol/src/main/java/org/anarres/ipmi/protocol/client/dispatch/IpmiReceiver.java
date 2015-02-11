/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.dispatch;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.session.IpmiSession;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;

/**
 *
 * @author shevek
 */
public interface IpmiReceiver {

    public void receive(@Nonnull IpmiHandlerContext context, @CheckForNull IpmiSession session, @Nonnull IpmiPayload response);

    public void timeout(@Nonnull IpmiReceiverKey key);

}
