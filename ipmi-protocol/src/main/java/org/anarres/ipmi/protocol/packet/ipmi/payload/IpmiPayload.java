/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.payload;

import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiPayloadHandler;
import org.anarres.ipmi.protocol.packet.common.Wireable;

/**
 * Various things which can be sent in IPMI packets: Session setup, authentication, commands, etc.
 *
 * @author shevek
 */
public interface IpmiPayload extends Wireable {

    @Nonnull
    public IpmiPayloadType getPayloadType();

    public void apply(@Nonnull IpmiClientIpmiPayloadHandler handler);
}
