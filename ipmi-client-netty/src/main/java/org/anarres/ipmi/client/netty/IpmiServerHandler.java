/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.client.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.rcmp.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author shevek
 */
public class IpmiServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(IpmiServerHandler.class);
    private final IpmiPipelineInitializer.SharedHandlers sharedHandlers;

    public IpmiServerHandler(@Nonnull IpmiPipelineInitializer.SharedHandlers sharedHandlers) {
        this.sharedHandlers = sharedHandlers;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            final Packet packet = (Packet) msg;
            Channel channel = ctx.channel();

        } catch (Exception e) {
            ctx.fireExceptionCaught(e);
            throw e;
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error("Error on channel: " + cause, cause);
        // LOG.error("Reported here: " + cause, new Exception("Here"));
    }
}
