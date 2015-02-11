/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiCommandHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiLun;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;

/**
 * An IPMI command, with subtypes {@link IpmiRequest} and {@link IpmiResponse}.
 *
 * @author shevek
 */
public interface IpmiCommand extends IpmiPayload {

    public static final int SEQUENCE_NUMBER_MASK = 0x3F;

    public void apply(@Nonnull IpmiClientIpmiCommandHandler handler, @CheckForNull IpmiHandlerContext context);

    public byte getTargetAddress();

    @Nonnull
    public IpmiLun getTargetLun();

    public byte getSourceAddress();

    @Nonnull
    public IpmiLun getSourceLun();

    /**
     * The IPMI sequence number.
     * NOT the session sequence number.
     * Between 0 and 63.
     *
     * @see #SEQUENCE_NUMBER_MASK
     */
    @Nonnegative
    public byte getSequenceNumber();

    /**
     * Sets the IPMI sequence number.
     *
     * This method masks the sequence number with {@link #SEQUENCE_NUMBER_MASK}.
     *
     * @param sequenceNumber The number to set.
     */
    public void setSequenceNumber(@Nonnegative byte sequenceNumber);

    @Nonnull
    public IpmiCommandName getCommandName();
}
