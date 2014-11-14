/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.common;

import com.google.common.base.Preconditions;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public abstract class AbstractWireable implements Wireable {

    protected abstract void toWireUnchecked(@Nonnull ByteBuffer buffer);

    @Override
    public void toWire(@Nonnull ByteBuffer buffer) {
        Preconditions.checkNotNull(buffer, "ByteBuffer was null.");
        int start = buffer.position();
        toWireUnchecked(buffer);

        int expectedLength = getWireLength();
        int actualLength = buffer.position() - start;
        if (actualLength != expectedLength)
            throw new IllegalStateException("Object should serialize to " + expectedLength + " bytes, but generated " + actualLength + ": " + this);
    }
}