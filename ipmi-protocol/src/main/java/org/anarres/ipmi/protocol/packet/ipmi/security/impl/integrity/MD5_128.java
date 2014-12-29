/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security.impl.integrity;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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
    public void update(ByteBuffer input) throws IllegalStateException {
        digest.update(input);
    }

    @Override
    public byte[] doFinal() throws IllegalStateException {
        byte[] out = digest.digest(key);
        int requestedMacLength = getName().getMacLength();
        if (requestedMacLength != out.length)
            out = Arrays.copyOf(out, requestedMacLength);
        return out;
    }
}
