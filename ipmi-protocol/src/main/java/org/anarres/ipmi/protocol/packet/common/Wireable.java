/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.common;

import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.session.IpmiPacketContext;

/**
 * An object which may be written to and read from a {@link ByteBuffer}.
 *
 * @author shevek
 */
public interface Wireable {

    /**
     * Returns the wire length of this packet.
     *
     * This must be exact, and not an overestimate.
     * If {@link #toWire(IpmiPacketContext, java.nio.ByteBuffer)} writes fewer bytes than the
     * computed length, the base class will assume error in the serializer
     * and throw an exception.
     * 
     * It is generally not valid to call getWireLength(IpmiPacketContext) on a Wireable which
     * does not contain complete data, as the length of data-dependent fields
     * cannot be computed.
	 *
	 * @param context The IpmiPacketContext for this operation.
     */
    @Nonnegative
    public int getWireLength(@Nonnull IpmiPacketContext context);

    /**
     * Writes bytes from this object onto the wire, serializing it.
	 *
	 * @param context The IpmiPacketContext for this operation.
	 * @param buffer The ByteBuffer into which this Wireable will be serialized.
     */
    public void toWire(@Nonnull IpmiPacketContext context, @Nonnull ByteBuffer buffer);

    /**
     * Reads bytes from the wire into this object, deserializing it.
	 *
	 * @param context The IpmiPacketContext for this operation.
	 * @param buffer The ByteBuffer from which this Wireable will be deserialized.
     */
    public void fromWire(@Nonnull IpmiPacketContext context, @Nonnull ByteBuffer buffer);

    public void toStringBuilder(@Nonnull StringBuilder buf, @Nonnegative int depth);
}
