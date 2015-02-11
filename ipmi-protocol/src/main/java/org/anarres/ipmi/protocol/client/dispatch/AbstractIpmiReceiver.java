/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.dispatch;

import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.session.IpmiSession;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;

/**
 *
 * @author shevek
 */
public abstract class AbstractIpmiReceiver<T extends IpmiPayload> implements IpmiReceiver {

    private final Class<T> payloadType;

    public AbstractIpmiReceiver(@Nonnull Class<T> payloadType) {
        this.payloadType = payloadType;
    }

    public Class<T> getPayloadType() {
        return payloadType;
    }

    @Override
    public final void receive(@Nonnull IpmiHandlerContext context, @Nonnull IpmiSession session, @Nonnull IpmiPayload response) {
        doReceive(context, session, getPayloadType().cast(response));
    }

    protected abstract void doReceive(@Nonnull IpmiHandlerContext context, @Nonnull IpmiSession session, @Nonnull T response);
}
