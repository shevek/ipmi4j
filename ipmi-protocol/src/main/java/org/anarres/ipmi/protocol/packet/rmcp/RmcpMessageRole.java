/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.rmcp;

import org.anarres.ipmi.protocol.packet.common.Code;

/**
 *
 * @author shevek
 */
public enum RmcpMessageRole implements Code.Wrapper {

    REQ {
        @Override
        public byte getCode() {
            return 0;
        }
    }, ACK {
        @Override
        public byte getCode() {
            // TODO: -> 0x80 and correct all else to match.
            return 1;
        }
    };
}
