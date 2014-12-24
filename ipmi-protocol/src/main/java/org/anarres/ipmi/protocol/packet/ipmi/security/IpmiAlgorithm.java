/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security;

import java.security.NoSuchAlgorithmException;
import javax.annotation.Nonnull;
import javax.crypto.NoSuchPaddingException;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 *
 * @author shevek
 */
public interface IpmiAlgorithm<Implementation> extends Code.Wrapper {

    /** [IPMI2] Section 13.7, table 13-9, page 147. */
    public byte getPayloadType();

    @Nonnull
    public Implementation newImplementation() throws NoSuchAlgorithmException, NoSuchPaddingException;
}
