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
import java.util.concurrent.ThreadFactory;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.engine.AbstractIpmiClient;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiContext;

/**
 *
 * @author shevek
 */
public class IpmiClient extends AbstractIpmiClient {

    private Channel channel;
    private IpmiPipelineInitializer.SharedHandlers sharedHandlers;
    private IpmiChannelType channelType = IpmiChannelType.NIO;

    public IpmiClient(@Nonnull IpmiContext context) {
        this.sharedHandlers = new IpmiPipelineInitializer.SharedHandlers(context);
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

        ThreadFactory factory = new DefaultThreadFactory("tftp-server");
        EventLoopGroup group = mode.newEventLoopGroup(factory);

        Bootstrap b = new Bootstrap();
        b.group(group);
        b.channel(mode.getChannelType());
        b.handler(new IpmiPipelineInitializer(sharedHandlers, new IpmiServerHandler(sharedHandlers)));
        channel = b.bind(0).sync().channel();
    }

    @Override
    public void stop() throws IOException, InterruptedException {
        EventLoop loop = channel.eventLoop();
        channel.close().sync();
        loop.shutdownGracefully();
    }
}
