/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.rmcp;

import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Wireable;

/**
 * RMCP Data.
 * 
 * [ASF2] Section 3.2.2.3 page 22.
 *
 * @author shevek
 */
public interface RmcpData extends Wireable {

    @Nonnull
    public RmcpMessageClass getMessageClass();
}
