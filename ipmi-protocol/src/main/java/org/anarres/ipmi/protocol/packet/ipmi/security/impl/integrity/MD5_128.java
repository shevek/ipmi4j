/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security.impl.integrity;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.ShortBufferException;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiIntegrityAlgorithm;

/**
 *
 * @author shevek
 */
public class MD5_128 implements MAC {

    private final MessageDigest digest;
    private byte[] key;

    public MD5_128() throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance("MD5");
    }

    @Override
    public IpmiIntegrityAlgorithm getName() {
        return IpmiIntegrityAlgorithm.MD5_128;
    }

    @Override
    public void init(byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
        digest.update(key);
        this.key = key;
    }

    @Override
    public void update(byte[] data, int off, int len) throws IllegalStateException {
        digest.update(data, off, len);
    }

    @Override
    public void doFinal(byte[] out, int off) throws ShortBufferException, IllegalStateException {
        byte[] tmp = digest.digest(key);
        System.arraycopy(tmp, 0, out, off, Math.min(tmp.length, getName().getMacLength()));
        this.key = null;
    }
}
