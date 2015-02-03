/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiCommandHandler;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiSession;

/**
 * An IPMI command, with subtypes {@link IpmiRequest} and {@link IpmiResponse}.
 *
 * @author shevek
 */
public interface IpmiCommand extends IpmiPayload {

    public void apply(@Nonnull IpmiClientIpmiCommandHandler handler, @CheckForNull IpmiSession session);
}
