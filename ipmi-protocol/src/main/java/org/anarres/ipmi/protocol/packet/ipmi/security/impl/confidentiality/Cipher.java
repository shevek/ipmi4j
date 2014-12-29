/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security.impl.confidentiality;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
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

    /**
     * @see javax.crypto.Cipher#init(int, Key, AlgorithmParameterSpec)
     */
    public void init(@Nonnull Mode mode, @Nonnull byte[] key, @Nonnull byte[] iv)
            throws InvalidKeyException, InvalidAlgorithmParameterException;

    /**
     * @see javax.crypto.Cipher#update(ByteBuffer, ByteBuffer)
     */
    public int update(@Nonnull ByteBuffer input, @Nonnull ByteBuffer output)
            throws ShortBufferException;
}
