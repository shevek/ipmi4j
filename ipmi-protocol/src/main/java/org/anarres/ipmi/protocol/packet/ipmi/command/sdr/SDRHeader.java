/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sdr;

import org.anarres.ipmi.protocol.packet.common.AbstractWireable;

/**
 * [IPMI2] Section 43.1, table 43-1, pages 524.
 *
 * @author shevek
 */
public abstract class SDRHeader extends AbstractWireable {

    public char recordId;
    // recordVersion == 0x51.
    // recordType: getter
    /** Autocomputed if zero. */
    public byte recordLength = 0;
}
