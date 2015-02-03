/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security.impl.authentication;

import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiAuthenticationAlgorithm;

/**
 *
 * @author shevek
 */
public interface Hash {

    @Nonnull
    public IpmiAuthenticationAlgorithm getName();

    public void update(@Nonnull ByteBuffer input);

    @Nonnull
    public byte[] doFinal();
}
