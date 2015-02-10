/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.anarres.ipmi.protocol.packet.rmcp.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author shevek
 */
public abstract class AbstractIpmiClient implements IpmiClient {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractIpmiClient.class);

    @PostConstruct
    public abstract void start() throws IOException, InterruptedException;

    // public abstract void send(@Nonnull Packet packet) throws IOException;
    public void receive(Packet packet) throws IOException {
        LOG.info("Receive\n" + packet);
    }

    @PreDestroy
    public abstract void stop() throws IOException, InterruptedException;
}
