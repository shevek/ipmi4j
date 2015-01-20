/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command;

import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;

/**
 *
 * @author shevek
 */
public class UnknownIpmiResponse extends UnknownIpmiCommand implements IpmiResponse {

    public UnknownIpmiResponse(@Nonnull IpmiCommandName commandName) {
        super(commandName);
    }
}
