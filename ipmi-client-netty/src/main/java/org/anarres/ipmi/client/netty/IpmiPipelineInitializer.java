/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.client.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LoggingHandler;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author shevek
 */
public class IpmiPipelineInitializer extends ChannelInitializer<Channel> {

    private static final Logger LOG = LoggerFactory.getLogger(IpmiPipelineInitializer.class);

    public static class SharedHandlers {
        // These are all singleton instances.

        // private final TftpExceptionHandler exceptionHandler = new TftpExceptionHandler();
        private final LoggingHandler wireLogger = new LoggingHandler("tftp-datagram");
        private final IpmiCodec codec = new IpmiCodec();
        private final LoggingHandler packetLogger = new LoggingHandler("tftp-packet");

        private boolean debug = false;

        public void setDebug(boolean debug) {
            this.debug = debug;
        }
    }
    private final SharedHandlers sharedHandlers;
    private final ChannelHandler handler;

    public IpmiPipelineInitializer(@Nonnull SharedHandlers sharedHandlers, @Nonnull ChannelHandler handler) {
        this.sharedHandlers = sharedHandlers;
        this.handler = handler;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (sharedHandlers.debug)
            pipeline.addLast(sharedHandlers.wireLogger);
        pipeline.addLast(sharedHandlers.codec);
        if (sharedHandlers.debug)
            pipeline.addLast(sharedHandlers.packetLogger);
        pipeline.addLast(handler);
    }
}
