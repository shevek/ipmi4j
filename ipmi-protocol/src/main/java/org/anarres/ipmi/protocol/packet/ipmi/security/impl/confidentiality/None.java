/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security.impl.confidentiality;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import javax.crypto.ShortBufferException;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiConfidentialityAlgorithm;

/**
 * [IPMI2] Section 13.28.5, page 159.
 *
 * @author shevek
 */
public class None implements Cipher {

    @Override
    public IpmiConfidentialityAlgorithm getName() {
        return IpmiConfidentialityAlgorithm.NONE;
    }

    @Override
    public int getIVSize() {
        return 0;
    }

    @Override
    public int getBlockSize() {
        return 1;
    }

    @Override
    public void init(Mode mode, byte[] key, byte[] iv) throws InvalidKeyException, InvalidAlgorithmParameterException {
    }

    @Override
    public int update(ByteBuffer input, ByteBuffer output) throws ShortBufferException {
        int remaining = input.remaining();
        output.put(input);
        return remaining;
    }
}