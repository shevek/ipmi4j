/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.payload;

import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public abstract class TempIpmiWireable {

    // @Nonnegative public abstract int getDataWireLength();

    /** Serializes the IPMI data into this RMCP data. */
    protected abstract void toWireData(@Nonnull ByteBuffer buffer);

    // protected abstract void fromWireData(@Nonnull ByteBuffer buffer);
}
