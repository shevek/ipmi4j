/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public interface IpmiSessionWrapper {

    public int getIpmiSessionId();

    public int getIpmiSessionSequenceNumber();

    @Nonnegative
    public int getWireLength(@Nonnull IpmiHeader header, @Nonnull IpmiSessionData data);

    public void toWire(@Nonnull ByteBuffer buffer, @Nonnull IpmiHeader header, @Nonnull IpmiSessionData data);

    public void fromWire(@Nonnull ByteBuffer buffer, @Nonnull IpmiHeader header, @Nonnull IpmiSessionData data);
}
