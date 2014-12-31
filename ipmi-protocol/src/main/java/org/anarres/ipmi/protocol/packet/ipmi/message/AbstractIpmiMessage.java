/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.message;

import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommand;

/**
 *
 * @author shevek
 */
public abstract class AbstractIpmiMessage extends AbstractWireable implements IpmiMessage {

    @Nonnull
    public abstract IpmiCommand getCommand();
}
