/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security;

import java.security.NoSuchAlgorithmException;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 *
 * @author shevek
 */
public interface IpmiAlgorithm<Implementation> extends Code.Wrapper {

    public byte getPayloadType();

    // @Nonnull public Implementation newImplementation() throws NoSuchAlgorithmException;
}
