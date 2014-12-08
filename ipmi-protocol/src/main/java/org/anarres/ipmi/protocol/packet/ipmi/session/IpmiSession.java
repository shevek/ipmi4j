/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.session;

/**
 *
 * @author shevek
 */
public class IpmiSession {

    private final int id;
    private int encryptedSequenceNumber;
    private int unencryptedSequenceNumber;

    public IpmiSession(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
