/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command;

import javax.annotation.Nonnull;

/**
 * An IPMI request.
 *
 * @author shevek
 */
public interface IpmiRequest extends IpmiCommand {

    @Nonnull
    public Class<? extends IpmiResponse> getResponseType();
}
