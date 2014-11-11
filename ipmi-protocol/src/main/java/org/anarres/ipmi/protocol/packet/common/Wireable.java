/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.common;

import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public interface Wireable {

    /**
     * Returns the wire length of this packet.
     * This must be exact, and not an overestimate.
     */
    @Nonnegative
    public int getWireLength();

    public void toWire(@Nonnull ByteBuffer buffer);
    // public void fromWire(@Nonnull ByteBuffer buffer);
}
