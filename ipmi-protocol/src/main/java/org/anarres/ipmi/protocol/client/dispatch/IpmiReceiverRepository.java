/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.dispatch;

import com.google.common.annotations.VisibleForTesting;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;

/**
 *
 * @author shevek
 */
public interface IpmiReceiverRepository {

    @CheckForNull
    @VisibleForTesting
    public IpmiReceiver getReceiver(@Nonnull IpmiHandlerContext context,
            @Nonnull Class<? extends IpmiPayload> payloadType, byte messageId);

    public void setReceiver(@Nonnull IpmiReceiverKey key, @Nonnull IpmiReceiver receiver);

}
