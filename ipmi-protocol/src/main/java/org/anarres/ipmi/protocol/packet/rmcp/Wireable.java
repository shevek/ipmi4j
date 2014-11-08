/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.rmcp;

import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public interface Wireable {

    @Nonnegative
    public int getWireLength();

    public void toWire(@Nonnull ByteBuffer buffer);

    public void fromWire(@Nonnull ByteBuffer buffer);
}
