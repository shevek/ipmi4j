/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client;

import java.net.InetSocketAddress;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.ipmi.command.IpmiRequest;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiSession;

/**
 *
 * @author shevek
 */
public interface IpmiClient {

    public void send(@Nonnull InetSocketAddress target, @Nonnull IpmiRequest request, @Nonnull IpmiClientResponseHandler responseHandler);

    public void connect(@Nonnull InetSocketAddress target, @Nonnull IpmiClientConnectHandler connectHandler);

    public void send(@Nonnull IpmiSession session, @Nonnull IpmiRequest request, @Nonnull IpmiClientResponseHandler responseHandler);
}
