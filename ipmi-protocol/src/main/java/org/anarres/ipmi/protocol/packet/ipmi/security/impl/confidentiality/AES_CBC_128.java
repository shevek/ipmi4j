/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security.impl.confidentiality;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiConfidentialityAlgorithm;

/**
 * [IPMI2] Section 13.29, page 160.
 *
 * @author shevek
 */
public class AES_CBC_128 extends AbstractJCECipher {

    public AES_CBC_128() throws NoSuchAlgorithmException, NoSuchPaddingException {
        super("AES/CBC/NoPadding");
    }

    @Override
    public IpmiConfidentialityAlgorithm getName() {
        return IpmiConfidentialityAlgorithm.AES_CBC_128;
    }

    @Override
    public void init(Mode mode, byte[] key, byte[] iv) throws InvalidKeyException, InvalidAlgorithmParameterException {
        super.init(mode, new SecretKeySpec(key, "AES"), iv);
    }
}
