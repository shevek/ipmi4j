/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.common;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * [IPMI2] Section 13.6, table 13-8, page 134.
 * 
 * Added as needed to cause the number of bytes in the data range covered by
 * the AuthCode (Integrity Data) field to be a multiple of 4 bytes (DWORD).
 * If present, each Integrity Pad byte is set to FFh.
 *
 * @author shevek
 */
public class Pad {

    private static final byte[] PAD0 = new byte[]{};
    private static final byte[] PAD1 = new byte[]{(byte) 0xFF};
    private static final byte[] PAD2 = new byte[]{(byte) 0xFF, (byte) 0xFF};
    private static final byte[] PAD3 = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF};

    @Nonnull
    public static byte[] PAD(@Nonnegative int length) {
        switch (length & 0x03) {
            case 0:
                return PAD0;
            case 1:
                return PAD3;
            case 2:
                return PAD2;
            case 3:
                return PAD1;
            default:
                throw new IllegalStateException("Illegal length " + length);
        }
    }
}
