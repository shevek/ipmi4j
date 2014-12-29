/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security.impl.confidentiality;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.annotation.Nonnull;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author shevek
 */
public abstract class AbstractJCECipher implements Cipher {

    private final javax.crypto.Cipher cipher;

    public AbstractJCECipher(@Nonnull String algorithm) throws NoSuchAlgorithmException, NoSuchPaddingException {
        this.cipher = javax.crypto.Cipher.getInstance(algorithm);
    }

    @Override
    public int getIVSize() {
        return cipher.getIV().length;
    }

    @Override
    public int getBlockSize() {
        return cipher.getBlockSize();
    }

    protected void init(Mode mode, SecretKeySpec key, byte[] iv) throws InvalidKeyException, InvalidAlgorithmParameterException {
        final int cipherMode;
        switch (mode) {
            case ENCRYPT:
                cipherMode = javax.crypto.Cipher.ENCRYPT_MODE;
                break;
            case DECRYPT:
                cipherMode = javax.crypto.Cipher.DECRYPT_MODE;
                break;
            default:
                throw new IllegalArgumentException("Illegal mode " + mode);
        }
        cipher.init(cipherMode, key, new IvParameterSpec(iv));
    }

    @Override
    public int update(ByteBuffer input, ByteBuffer output) throws ShortBufferException {
        return cipher.update(input, output);
    }
}