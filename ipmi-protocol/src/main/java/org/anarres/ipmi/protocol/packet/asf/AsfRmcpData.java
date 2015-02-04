/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientAsfMessageHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.rmcp.RmcpData;

/**
 *
 * @author shevek
 */
public interface AsfRmcpData extends RmcpData {

    /** [ASF2] Page 22. */
    @Nonnull
    public AsfRmcpMessageType getMessageType();

    public void apply(@Nonnull IpmiClientAsfMessageHandler handler, IpmiHandlerContext context);
}
