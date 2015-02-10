/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author shevek
 */
public class ShiftTest {

    private static final Logger LOG = LoggerFactory.getLogger(ShiftTest.class);

    @Test
    public void testShift() {
        byte b = (byte) 0b10000000;
        LOG.info("Shift is " + (b >>> 7));
    }
}
