/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.payload;

/**
 *
 * @author shevek
 */
public abstract class AbstractTaggedIpmiPayload extends AbstractIpmiPayload {

    protected byte messageTag;

    public byte getMessageTag() {
        return messageTag;
    }

    public void setMessageTag(byte messageTag) {
        this.messageTag = messageTag;
    }
}
