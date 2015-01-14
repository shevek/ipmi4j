/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.common;

import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiContext;

/**
 *
 * @author shevek
 */
public interface Wireable {

    /**
     * Returns the wire length of this packet.
     *
     * This must be exact, and not an overestimate.
     * If {@link #toWire(java.nio.ByteBuffer)} writes fewer bytes than the
     * computed length, the base class will assume error in the serializer
     * and throw an exception.
     * 
     * It is generally not valid to call getWireLength() on a Wireable which
     * does not contain complete data, as the length of data-dependent fields
     * cannot be computed.
     */
    @Nonnegative
    public int getWireLength(@Nonnull IpmiContext context);

    /**
     * Writes bytes from this object onto the wire, serializing it.
     */
    public void toWire(@Nonnull IpmiContext context, @Nonnull ByteBuffer buffer);

    /**
     * Reads bytes from the wire into this object, deserializing it.
     */
    public void fromWire(@Nonnull IpmiContext context, @Nonnull ByteBuffer buffer);

    public void toStringBuilder(@Nonnull StringBuilder buf, @Nonnegative int depth);
}
