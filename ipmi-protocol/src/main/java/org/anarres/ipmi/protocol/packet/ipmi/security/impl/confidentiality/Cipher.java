/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security.impl.confidentiality;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.crypto.ShortBufferException;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiConfidentialityAlgorithm;

/**
 *
 * @author shevek
 */
public interface Cipher {

    public enum Mode {

        ENCRYPT, DECRYPT;
    }

    @Nonnull
    public IpmiConfidentialityAlgorithm getName();

    @Nonnegative
    public int getIVSize();

    @Nonnegative
    public int getBlockSize();

    public void init(@Nonnull Mode mode, @Nonnull byte[] key, @Nonnull byte[] iv)
            throws InvalidKeyException, InvalidAlgorithmParameterException;

    public int update(
            @Nonnull byte[] input, @Nonnegative int inputOffset, @Nonnegative int inputLen,
            @Nonnull byte[] output, @Nonnegative int outputOffset)
            throws ShortBufferException;
}
