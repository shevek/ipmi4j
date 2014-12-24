/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.payload;

import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Wireable;

/**
 *
 * @author shevek
 */
public interface IpmiPayload extends Wireable {

    @Nonnull
    public IpmiPayloadType getPayloadType();
}
