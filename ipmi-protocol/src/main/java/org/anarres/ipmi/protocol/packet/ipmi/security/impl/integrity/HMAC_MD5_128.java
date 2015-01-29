/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security.impl.integrity;

import java.security.NoSuchAlgorithmException;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiIntegrityAlgorithm;

/**
 * [IPMI2] Section 13.28.4, page 158.
 *
 * @author shevek
 */
public class HMAC_MD5_128 extends AbstractJCEMAC {

    public HMAC_MD5_128() throws NoSuchAlgorithmException {
        super("HmacMD5");
    }

    @Override
    public IpmiIntegrityAlgorithm getName() {
        return IpmiIntegrityAlgorithm.HMAC_MD5_128;
    }
}
