/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.session;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiSessionAuthenticationType;
import org.anarres.ipmi.protocol.packet.ipmi.command.messaging.GetChannelAuthenticationCapabilitiesResponse;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiAuthenticationAlgorithm;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiConfidentialityAlgorithm;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiIntegrityAlgorithm;

/**
 *
 * @author shevek
 */
public class IpmiSession {

    private IpmiSessionState state = IpmiSessionState.UNKNOWN;
    private final int consoleSessionId;
    public GetChannelAuthenticationCapabilitiesResponse channelAuthenticationCapabilities;
    private int systemSessionId;
    private AtomicInteger encryptedSequenceNumber = new AtomicInteger(0);
    private AtomicInteger unencryptedSequenceNumber = new AtomicInteger(0);
    private IpmiSessionAuthenticationType authenticationType = IpmiSessionAuthenticationType.RMCPP;
    private IpmiAuthenticationAlgorithm authenticationAlgorithm;
    private IpmiConfidentialityAlgorithm confidentialityAlgorithm;
    private IpmiConfidentialityAlgorithm.State confidentialityAlgorithmState;
    private IpmiIntegrityAlgorithm integrityAlgorithm;

    public IpmiSession(int consoleSessionId) {
        this.consoleSessionId = consoleSessionId;
    }

    public int getConsoleSessionId() {
        return consoleSessionId;
    }

    public int getSystemSessionId() {
        return systemSessionId;
    }

    public void setSystemSessionId(int systemSessionId) {
        this.systemSessionId = systemSessionId;
    }

    public int nextUnencryptedSequenceNumber() {
        return unencryptedSequenceNumber.getAndIncrement();
    }

    public int nextEncryptedSequenceNumber() {
        return encryptedSequenceNumber.getAndIncrement();
    }

    @Nonnull
    public byte[] newRandomSeed(int length) {
        SecureRandom r = new SecureRandom();
        return r.generateSeed(length);
    }

    @Nonnull
    public IpmiSessionAuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public IpmiAuthenticationAlgorithm getAuthenticationAlgorithm() {
        return authenticationAlgorithm;
    }

    public void setAuthenticationAlgorithm(IpmiAuthenticationAlgorithm authenticationAlgorithm) {
        this.authenticationAlgorithm = authenticationAlgorithm;
    }

    public IpmiConfidentialityAlgorithm getConfidentialityAlgorithm() {
        return confidentialityAlgorithm;
    }

    public void setConfidentialityAlgorithm(IpmiConfidentialityAlgorithm confidentialityAlgorithm) {
        this.confidentialityAlgorithm = confidentialityAlgorithm;
    }

    public IpmiConfidentialityAlgorithm.State getConfidentialityAlgorithmState() {
        return confidentialityAlgorithmState;
    }

    public void setConfidentialityAlgorithmState(IpmiConfidentialityAlgorithm.State confidentialityAlgorithmState) {
        this.confidentialityAlgorithmState = confidentialityAlgorithmState;
    }

    public IpmiIntegrityAlgorithm getIntegrityAlgorithm() {
        return integrityAlgorithm;
    }

    public void setIntegrityAlgorithm(IpmiIntegrityAlgorithm integrityAlgorithm) {
        this.integrityAlgorithm = integrityAlgorithm;
    }

    /** [IPMI2] Section 13.32, page 165. */
    @Nonnull
    public byte[] getAdditionalKey(@Nonnegative int idx) throws NoSuchAlgorithmException {
        IpmiAuthenticationAlgorithm algorithm = getAuthenticationAlgorithm();
        byte[] raw = new byte[algorithm.getHashLength()];   // Should this be 20, or the hash length?
        Arrays.fill(raw, UnsignedBytes.checkedCast(idx));
        return algorithm.hash(ByteBuffer.wrap(raw));
    }
}
