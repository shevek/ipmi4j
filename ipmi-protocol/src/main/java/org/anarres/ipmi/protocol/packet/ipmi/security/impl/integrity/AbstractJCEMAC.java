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
public abstract class AbstractJCEMAC implements MAC {

    private final Mac mac;

    protected AbstractJCEMAC(@Nonnull String algorithm) throws NoSuchAlgorithmException {
        mac = Mac.getInstance(algorithm);
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
        int macLength = getName().getMacLength();
        if (macLength != mac.getMacLength()) {
            byte[] tmp = new byte[mac.getMacLength()];
            doFinal(tmp, 0);
            System.arraycopy(tmp, 0, out, offset, macLength);
        } else {
            mac.doFinal(out, offset);
        }
    }
}