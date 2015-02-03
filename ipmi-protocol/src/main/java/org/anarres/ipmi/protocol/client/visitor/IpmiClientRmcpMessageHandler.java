/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.visitor;

import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.asf.AsfRmcpData;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiSessionWrapper;
import org.anarres.ipmi.protocol.packet.rmcp.OEMRmcpMessage;
import org.anarres.ipmi.protocol.packet.rmcp.RmcpData;

/**
 *
 * @author shevek
 */
public interface IpmiClientRmcpMessageHandler {

    public static class Adapter implements IpmiClientRmcpMessageHandler {

        public void handleDefault(@Nonnull RmcpData message) {
        }

        @Override
        public void handleAsfRmcpData(AsfRmcpData message) {
            handleDefault(message);
        }

        @Override
        public void handleIpmiRmcpData(IpmiSessionWrapper message) {
            handleDefault(message);
        }

        @Override
        public void handleOemRmcpData(OEMRmcpMessage message) {
            handleDefault(message);
        }
    }

    public void handleAsfRmcpData(@Nonnull AsfRmcpData message);

    public void handleIpmiRmcpData(@Nonnull IpmiSessionWrapper message);

    public void handleOemRmcpData(@Nonnull OEMRmcpMessage message);
}
