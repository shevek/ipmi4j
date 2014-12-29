/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security.impl.authentication;

import javax.annotation.Nonnull;
import javax.crypto.ShortBufferException;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiAuthenticationAlgorithm;

/**
 *
 * @author shevek
 */
public interface Hash {

    @Nonnull
    public IpmiAuthenticationAlgorithm getName();

    @Nonnull
    public byte[] doFinal() throws ShortBufferException;
}
