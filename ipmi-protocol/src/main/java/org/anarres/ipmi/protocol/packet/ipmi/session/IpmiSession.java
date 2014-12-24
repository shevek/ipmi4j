/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.session;

import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiAuthenticationAlgorithm;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiConfidentialityAlgorithm;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiIntegrityAlgorithm;

/**
 *
 * @author shevek
 */
public class IpmiSession {

    private final int id;
    private int encryptedSequenceNumber;
    private int unencryptedSequenceNumber;
    private IpmiAuthenticationAlgorithm authenticationAlgorithm;
    private IpmiConfidentialityAlgorithm confidentialityAlgorithm;
    private IpmiIntegrityAlgorithm integrityAlgorithm;

    public IpmiSession(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
