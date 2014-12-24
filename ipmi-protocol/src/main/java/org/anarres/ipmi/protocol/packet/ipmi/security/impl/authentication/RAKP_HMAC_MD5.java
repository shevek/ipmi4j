/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security.impl.authentication;

import java.security.NoSuchAlgorithmException;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiAuthenticationAlgorithm;

/**
 *
 * @author shevek
 */
public class RAKP_HMAC_MD5 extends AbstractJCEHash {

    public RAKP_HMAC_MD5() throws NoSuchAlgorithmException {
        super("HmacMD5");
    }

    @Override
    public IpmiAuthenticationAlgorithm getName() {
        return IpmiAuthenticationAlgorithm.RAKP_HMAC_MD5;
    }
}