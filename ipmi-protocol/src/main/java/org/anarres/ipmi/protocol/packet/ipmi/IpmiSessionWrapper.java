/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiPayloadHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;
import org.anarres.ipmi.protocol.packet.rmcp.RmcpData;

/**
 * [IPMI2] Section 13.6, page 132, table 13-8.
 *
 * @author shevek
 */
public interface IpmiSessionWrapper extends RmcpData {

    public int getIpmiSessionId();

    public void setIpmiSessionId(int ipmiSessionId);

    public int getIpmiSessionSequenceNumber();

    public void setIpmiSessionSequenceNumber(int ipmiSessionSequenceNumber);

    public IpmiPayload getIpmiPayload();

    public void setIpmiPayload(@Nonnull IpmiPayload ipmiPayload);

    public void apply(@Nonnull IpmiClientIpmiPayloadHandler handler, IpmiHandlerContext context);
}