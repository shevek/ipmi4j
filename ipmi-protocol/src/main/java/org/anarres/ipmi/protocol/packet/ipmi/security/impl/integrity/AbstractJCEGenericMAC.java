/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security.impl.integrity;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.annotation.Nonnull;
import javax.crypto.Mac;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author shevek
 */
public abstract class AbstractJCEGenericMAC implements GenericMAC {

    private final Mac mac;

    protected AbstractJCEGenericMAC(@Nonnull String algorithm) throws NoSuchAlgorithmException {
        mac = Mac.getInstance(algorithm);
    }

    @Nonnull
    protected Mac getMac() {
        return mac;
    }

    @Override
    public void init(byte[] key) throws InvalidKeyException {
        SecretKeySpec skey = new SecretKeySpec(key, mac.getAlgorithm());
        mac.init(skey);
    }

    @Override
    public void update(byte[] data, int off, int len) {
        mac.update(data, off, len);
    }

    @Override
    public void doFinal(byte[] out, int offset) throws ShortBufferException {
        mac.doFinal(out, offset);
    }
}