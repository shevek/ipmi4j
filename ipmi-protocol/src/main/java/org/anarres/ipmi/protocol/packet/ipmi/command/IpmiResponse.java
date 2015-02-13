/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command;

import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCompletionCode;

/**
 * An IPMI response.
 *
 * An IPMI response has a {@link IpmiCompletionCode}, which some implementations
 * consider to be part of the header, but is a part of the response body according
 * to the spec.
 * 
 * @see AbstractIpmiResponse
 * @author shevek
 */
public interface IpmiResponse extends IpmiCommand {

    @Nonnull
    public Class<? extends IpmiRequest> getRequestType();
    // public byte getCompletionCode();
}
