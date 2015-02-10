/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.client.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.AbstractIpmiClient;
import org.anarres.ipmi.protocol.client.session.IpmiContext;
import org.anarres.ipmi.protocol.client.session.IpmiSessionManager;
import org.anarres.ipmi.protocol.packet.rmcp.Packet;
import org.anarres.ipmi.protocol.packet.rmcp.RmcpPacket;

/**
 *
 * @author shevek
 */
public class IpmiClientImpl extends AbstractIpmiClient {

    private Channel channel;
    private IpmiPipelineInitializer.SharedHandlers sharedHandlers;
    private IpmiChannelType channelType = IpmiChannelType.NIO;

    public IpmiClientImpl(@Nonnull IpmiContext context) {
        this.sharedHandlers = new IpmiPipelineInitializer.SharedHandlers(context);
    }

    @Override
    public IpmiSessionManager getSessionManager() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDebug(boolean debug) {
        sharedHandlers.setDebug(debug);
    }

    @Nonnull
    public IpmiChannelType getChannelType() {
        return channelType;
    }

    public void setChannelType(@Nonnull IpmiChannelType channelType) {
        this.channelType = channelType;
    }

    @Override
    public void start() throws IOException, InterruptedException {
        IpmiChannelType mode = getChannelType();

        ThreadFactory factory = new DefaultThreadFactory("ipmi-client");
        EventLoopGroup group = mode.newEventLoopGroup(factory);

        Bootstrap b = new Bootstrap();
        b.group(group);
        b.channel(mode.getChannelType());
        b.handler(new IpmiPipelineInitializer(sharedHandlers, new IpmiClientHandler(this)));
        channel = b.bind(0).sync().channel();
    }

    @Override
    public void send(Packet packet) {
        Future<?> f = channel.writeAndFlush(packet, channel.voidPromise());
    }

    // @Override
    // public void send(IpmiSession session, IpmiRequest request, IpmiClientResponseHandler responseHandler) {
    // throw new UnsupportedOperationException("Not supported yet.");
    // }
    @Override
    public void stop() throws IOException, InterruptedException {
        EventLoop loop = channel.eventLoop();
        channel.close().sync();
        loop.shutdownGracefully();
    }
}
