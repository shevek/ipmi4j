/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security.impl.authentication;

import java.security.NoSuchAlgorithmException;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiAuthenticationAlgorithm;

/**
 * [IPMI2] Section 13.28.1b, page 158.
 *
 * @author shevek
 */
public class RAKP_HMAC_SHA256 extends AbstractJCEHash {

    public RAKP_HMAC_SHA256() throws NoSuchAlgorithmException {
        super("HmacSHA256");
    }

    @Override
    public IpmiAuthenticationAlgorithm getName() {
        return IpmiAuthenticationAlgorithm.RAKP_HMAC_SHA256;
    }
}