/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.alg;

import org.anarres.ipmi.protocol.packet.common.Code;

/**
 *
 * @author shevek
 */
public interface IpmiAlgorithm extends Code.Wrapper {

    public byte getPayloadType();
}
