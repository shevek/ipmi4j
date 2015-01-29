/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security.impl.integrity;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiIntegrityAlgorithm;

/**
 * [IPMI2] Section 13.28.4, page 158.
 *
 * @author shevek
 */
public class None implements MAC {

    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    @Override
    public IpmiIntegrityAlgorithm getName() {
        return IpmiIntegrityAlgorithm.NONE;
    }

    @Override
    public void init(byte[] key) {
    }

    @Override
    public void update(ByteBuffer input) {
    }

    @Override
    public byte[] doFinal() {
        return EMPTY_BYTE_ARRAY;
    }
}
