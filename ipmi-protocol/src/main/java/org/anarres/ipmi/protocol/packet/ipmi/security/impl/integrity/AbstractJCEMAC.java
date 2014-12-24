/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security.impl.integrity;

import java.security.NoSuchAlgorithmException;
import javax.annotation.Nonnull;
import javax.crypto.ShortBufferException;

/**
 *
 * @author shevek
 */
public abstract class AbstractJCEMAC extends AbstractJCEGenericMAC implements MAC {

    public AbstractJCEMAC(@Nonnull String algorithm) throws NoSuchAlgorithmException {
        super(algorithm);
    }

    @Override
    public void doFinal(byte[] out, int offset) throws ShortBufferException {
        int requestedMacLength = getName().getMacLength();
        int actualMacLength = getMac().getMacLength();
        if (requestedMacLength != actualMacLength) {
            byte[] tmp = new byte[requestedMacLength];
            super.doFinal(tmp, 0);
            System.arraycopy(tmp, 0, out, offset, requestedMacLength);
        } else {
            super.doFinal(out, offset);
        }
    }
}
