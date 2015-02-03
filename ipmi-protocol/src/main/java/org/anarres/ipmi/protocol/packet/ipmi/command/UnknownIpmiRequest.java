/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command;

import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiCommandHandler;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiSession;

/**
 *
 * @author shevek
 */
public class UnknownIpmiRequest extends UnknownIpmiCommand implements IpmiRequest {

    public UnknownIpmiRequest(@Nonnull IpmiCommandName commandName) {
        super(commandName);
    }

    @Override
    public void apply(IpmiClientIpmiCommandHandler handler, IpmiSession session) {
    }
}
