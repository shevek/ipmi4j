/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.visitor;

import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.ipmi.command.IpmiCommand;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiOpenSessionRequest;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiOpenSessionResponse;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiRAKPMessage1;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiRAKPMessage2;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiRAKPMessage3;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiRAKPMessage4;
import org.anarres.ipmi.protocol.packet.ipmi.payload.OemExplicit;
import org.anarres.ipmi.protocol.packet.ipmi.payload.SOLMessage;

/**
 *
 * @author shevek
 */
public interface IpmiClientIpmiPayloadHandler {

    public static class Adapter implements IpmiClientIpmiPayloadHandler {

        public void handleDefault(@Nonnull IpmiPayload payload) {
        }

        @Override
        public void handleOpenSessionRequest(IpmiOpenSessionRequest request) {
            handleDefault(request);
        }

        @Override
        public void handleOpenSessionResponse(IpmiOpenSessionResponse response) {
            handleDefault(response);
        }

        @Override
        public void handleRAKPMessage1(IpmiRAKPMessage1 message) {
            handleDefault(message);
        }

        @Override
        public void handleRAKPMessage2(IpmiRAKPMessage2 message) {
            handleDefault(message);
        }

        @Override
        public void handleRAKPMessage3(IpmiRAKPMessage3 message) {
            handleDefault(message);
        }

        @Override
        public void handleRAKPMessage4(IpmiRAKPMessage4 message) {
            handleDefault(message);
        }

        @Override
        public void handleOemExplicit(OemExplicit message) {
            handleDefault(message);
        }

        @Override
        public void handleCommand(IpmiCommand message) {
            handleDefault(message);
        }

        @Override
        public void handleSOL(SOLMessage message) {
            handleDefault(message);
        }
    }

    /*
     public static class Login implements IpmiClientPayloadHandler {

     @Override
     public void handlePayload(IpmiPayload payload) {
     throw new UnsupportedOperationException("Not supported yet.");
     }
     }
     */
    public void handleOpenSessionRequest(@Nonnull IpmiOpenSessionRequest request);

    public void handleOpenSessionResponse(@Nonnull IpmiOpenSessionResponse response);

    public void handleRAKPMessage1(@Nonnull IpmiRAKPMessage1 message);

    public void handleRAKPMessage2(@Nonnull IpmiRAKPMessage2 message);

    public void handleRAKPMessage3(@Nonnull IpmiRAKPMessage3 message);

    public void handleRAKPMessage4(@Nonnull IpmiRAKPMessage4 message);

    public void handleOemExplicit(@Nonnull OemExplicit message);

    public void handleCommand(@Nonnull IpmiCommand command);

    public void handleSOL(@Nonnull SOLMessage message);
    // /* Does not distinguish between {@link IpmiPayloadType#OEM0} through {@link IpmiPayloadType#OEM7}. */
    // public void handleOEM(@Nonnull OEMMessage message)
}
