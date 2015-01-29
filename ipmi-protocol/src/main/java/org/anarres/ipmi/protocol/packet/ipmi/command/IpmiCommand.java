/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command;

import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;

/**
 * An IPMI command, with subtypes {@link IpmiRequest} and {@link IpmiResponse}.
 *
 * @author shevek
 */
public interface IpmiCommand extends IpmiPayload {
}
