/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Wireable;

/**
 *
 * @author shevek
 */
public interface IpmiSessionWrapper {

    public int getIpmiSessionId();

    public int getIpmiSessionSequenceNumber();

    @Nonnull
    public Wireable getIpmiSessionHeader(@Nonnull IpmiData data);

    @Nonnull
    public Wireable getIpmiSessionTrailer(@Nonnull IpmiData data);
}
