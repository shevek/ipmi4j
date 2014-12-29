/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security.impl.authentication;

import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiAuthenticationAlgorithm;

/**
 *
 * @author shevek
 */
public class None implements Hash {

    @Override
    public IpmiAuthenticationAlgorithm getName() {
        return IpmiAuthenticationAlgorithm.RAKP_NONE;
    }

    @Override
    public byte[] doFinal() {
        return org.anarres.ipmi.protocol.packet.ipmi.security.impl.integrity.None.EMPTY_BYTE_ARRAY;
    }
}
