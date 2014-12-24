/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.rmcp.RmcpData;

/**
 * [IPMI2] Section 13.8 page 136.
 * [IPMI2] Section 7.3 page 69?
 *
 * @author shevek
 */
public interface IpmiData extends RmcpData {
    // IpmiSessionWrapper
    // IpmiHeader
    // Data

    @Nonnull
    public IpmiHeader getIpmiHeader();
}