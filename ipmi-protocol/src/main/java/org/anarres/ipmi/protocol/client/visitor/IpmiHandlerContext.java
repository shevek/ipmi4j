/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.visitor;

import java.net.InetSocketAddress;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.IpmiClient;
import org.anarres.ipmi.protocol.client.dispatch.AbstractIpmiReceiver;
import org.anarres.ipmi.protocol.client.dispatch.IpmiReceiver;
import org.anarres.ipmi.protocol.client.session.IpmiSession;
import org.anarres.ipmi.protocol.client.session.IpmiSessionManager;
import org.anarres.ipmi.protocol.packet.ipmi.Ipmi15SessionWrapper;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiSessionWrapper;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;
import org.anarres.ipmi.protocol.packet.rmcp.RmcpData;
import org.anarres.ipmi.protocol.packet.rmcp.RmcpPacket;

/**
 *
 * @author shevek
 */
public class IpmiHandlerContext {

    private final IpmiClient client;
    private final InetSocketAddress systemAddress;
    // private IpmiSession ipmiSession;

    public IpmiHandlerContext(@Nonnull IpmiClient client, @Nonnull InetSocketAddress systemAddress) {
        this.client = client;
        this.systemAddress = systemAddress;
    }

    @Nonnull
    public IpmiClient getClient() {
        return client;
    }

    @Nonnull
    public IpmiSessionManager getSessionManager() {
        return getClient().getSessionManager();
    }

    @Nonnull
    public InetSocketAddress getSystemAddress() {
        return systemAddress;
    }

    /*
     @CheckForNull
     public IpmiSession getIpmiSession() {
     return ipmiSession;
     }

     public void setIpmiSession(@Nonnull IpmiSession ipmiSession) {
     this.ipmiSession = ipmiSession;
     }
     */
    public void send(@Nonnull RmcpData data) {
        RmcpPacket packet = new RmcpPacket();
        packet.withRemoteAddress(getSystemAddress());
        packet.withData(data);
        client.send(packet);
    }

    public void send(@CheckForNull IpmiSession session, @Nonnull IpmiPayload payload,
            @Nonnull Class<? extends IpmiPayload> responseType, @Nonnull IpmiReceiver receiver) {
        IpmiSessionWrapper wrapper = new Ipmi15SessionWrapper();
        wrapper.setIpmiPayload(payload);
        send(wrapper);
    }

    public void send(@CheckForNull IpmiSession session, @Nonnull IpmiPayload payload,
            @Nonnull AbstractIpmiReceiver receiver) {
        send(session, payload, receiver.getPayloadType(), receiver);
    }
}
