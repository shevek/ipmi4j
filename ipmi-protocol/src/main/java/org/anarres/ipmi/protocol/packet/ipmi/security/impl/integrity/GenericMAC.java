/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security.impl.integrity;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.crypto.ShortBufferException;

/**
 *
 * @author shevek
 */
public interface GenericMAC {

    public void init(@Nonnull byte[] key) throws NoSuchAlgorithmException, InvalidKeyException;

    public void update(@Nonnull byte[] data, @Nonnegative int off, @Nonnegative int len) throws IllegalStateException;

    public void doFinal(@Nonnull byte[] out, @Nonnegative int off) throws ShortBufferException, IllegalStateException;
}
