/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security.impl.integrity;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public abstract class AbstractJCEMAC extends AbstractJCEGenericMAC implements MAC {

    public AbstractJCEMAC(@Nonnull String algorithm) throws NoSuchAlgorithmException {
        super(algorithm);
    }

    @Override
    public byte[] doFinal() {
        byte[] out = super.doFinal();
        int requestedMacLength = getName().getMacLength();
        if (requestedMacLength != out.length)
            out = Arrays.copyOf(out, requestedMacLength);
        return out;
    }
}
