/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.dispatch;

import com.google.common.base.MoreObjects;
import java.net.SocketAddress;
import java.util.Objects;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.ipmi.command.IpmiCommand;
import org.anarres.ipmi.protocol.packet.ipmi.payload.AbstractTaggedIpmiPayload;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;

/**
 *
 * @author shevek
 */
public class IpmiReceiverKey {

    private final SocketAddress remoteAddress;
    // TODO: SessionId?
    private final Class<? extends IpmiPayload> payloadType;
    private final byte messageId;

    /**
     * Constructs a new IpmiReceiverKey.
     *
     * @param remoteAddress The remote system address sending the response.
     * @param payloadType The class of the expected response.
     * @param messageId The {@link AbstractTaggedIpmiPayload#getMessageTag() message tag}
     * or {@link IpmiCommand#getSequenceNumber() sequence number} to match.
     */
    public IpmiReceiverKey(@Nonnull SocketAddress remoteAddress, @Nonnull Class<? extends IpmiPayload> payloadType, @Nonnegative byte messageId) {
        this.remoteAddress = remoteAddress;
        this.payloadType = payloadType;
        this.messageId = messageId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(remoteAddress, payloadType) ^ messageId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (null == obj)
            return false;
        if (!getClass().equals(obj.getClass()))
            return false;
        IpmiReceiverKey o = (IpmiReceiverKey) obj;
        return Objects.equals(remoteAddress, o.remoteAddress)
                && Objects.equals(payloadType, o.payloadType)
                && messageId == o.messageId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("remoteAddress", remoteAddress)
                .add("payloadType", payloadType)
                .add("messageId", messageId)
                .toString();
    }

}
