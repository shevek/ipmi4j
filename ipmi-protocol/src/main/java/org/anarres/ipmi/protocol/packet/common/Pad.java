/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.common;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public class Pad {

    private static final byte[] PAD0 = new byte[0];
    private static final byte[] PAD1 = new byte[1];
    private static final byte[] PAD2 = new byte[2];
    private static final byte[] PAD3 = new byte[3];

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
