/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security.impl.confidentiality;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import javax.crypto.ShortBufferException;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiConfidentialityAlgorithm;

/**
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
    public int update(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) throws ShortBufferException {
        if (output.length - outputOffset < inputLen)
            throw new ShortBufferException("Required " + inputLen + " bytes but only " + (output.length - outputOffset) + " available.");
        System.arraycopy(input, inputOffset, output, outputOffset, inputLen);
        return inputLen;
    }
}
