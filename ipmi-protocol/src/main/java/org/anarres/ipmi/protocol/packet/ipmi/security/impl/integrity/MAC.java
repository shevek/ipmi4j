/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security.impl.integrity;

import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiIntegrityAlgorithm;

/**
 *
 * @author shevek
 */
public interface MAC extends GenericMAC {

    @Nonnull
    public IpmiIntegrityAlgorithm getName();
}
