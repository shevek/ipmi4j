/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security.impl.integrity;

import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiIntegrityAlgorithm;

/**
 *
 * @author shevek
 */
public class None implements MAC {

    @Override
    public IpmiIntegrityAlgorithm getName() {
        return IpmiIntegrityAlgorithm.NONE;
    }

    @Override
    public void init(byte[] key) {
    }

    @Override
    public void update(byte[] data, int start, int len) {
    }

    @Override
    public void doFinal(byte[] out, int offset) {
    }
}
