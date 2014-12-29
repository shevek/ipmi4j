/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security.impl.integrity;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public interface GenericMAC {

    public void init(@Nonnull byte[] key) throws NoSuchAlgorithmException, InvalidKeyException;

    public void update(@Nonnull ByteBuffer input) throws IllegalStateException;

    @Nonnull
    public byte[] doFinal() throws IllegalStateException;
}
