/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import java.nio.ByteBuffer;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiSession;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiSessionManager;

/**
 *
 * @author shevek
 */
public interface IpmiSessionWrapper {

    // public int getIpmiSessionId(); 
    // public int getIpmiSessionSequenceNumber();
    @Nonnegative
    public int getWireLength(@CheckForNull IpmiSession session, @Nonnull IpmiPayload payload);

    public void toWire(@Nonnull ByteBuffer buffer, @CheckForNull IpmiSession session, @Nonnull IpmiPayload payload);

    public void fromWire(@Nonnull ByteBuffer buffer, @Nonnull IpmiSessionManager sessionManager, @Nonnull IpmiSessionData sessionData);
}
