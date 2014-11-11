/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.common;

import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.rmcp.Packet;

/**
 *
 * @author shevek
 */
public interface Payload extends Wireable {

    @Nonnull
    public Packet toPacket();
}
