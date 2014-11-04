/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.client.netty;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import java.util.concurrent.ThreadFactory;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public enum IpmiChannelType {

    NIO {
        @Override
        public EventLoopGroup newEventLoopGroup(ThreadFactory factory) {
            return new NioEventLoopGroup(0, factory);
        }

        @Override
        public Class<? extends DatagramChannel> getChannelType() {
            return NioDatagramChannel.class;
        }
    }, EPOLL {
        @Override
        public EventLoopGroup newEventLoopGroup(ThreadFactory factory) {
            return new EpollEventLoopGroup(0, factory);
        }

        @Override
        public Class<? extends DatagramChannel> getChannelType() {
            return EpollDatagramChannel.class;
        }
    };

    @Nonnull
    public abstract EventLoopGroup newEventLoopGroup(@Nonnull ThreadFactory factory);

    @Nonnull
    public abstract Class<? extends DatagramChannel> getChannelType();
}
