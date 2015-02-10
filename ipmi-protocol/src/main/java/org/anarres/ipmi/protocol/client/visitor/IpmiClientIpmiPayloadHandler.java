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

        public void handleDefault(@Nonnull IpmiHandlerContext context, @Nonnull IpmiPayload payload) {
        }

        @Override
        public void handleOpenSessionRequest(IpmiHandlerContext context, IpmiOpenSessionRequest message) {
            handleDefault(context, message);
        }

        @Override
        public void handleOpenSessionResponse(IpmiHandlerContext context, IpmiOpenSessionResponse message) {
            handleDefault(context, message);
        }

        @Override
        public void handleRAKPMessage1(IpmiHandlerContext context, IpmiRAKPMessage1 message) {
            handleDefault(context, message);
        }

        @Override
        public void handleRAKPMessage2(IpmiHandlerContext context, IpmiRAKPMessage2 message) {
            handleDefault(context, message);
        }

        @Override
        public void handleRAKPMessage3(IpmiHandlerContext context, IpmiRAKPMessage3 message) {
            handleDefault(context, message);
        }

        @Override
        public void handleRAKPMessage4(IpmiHandlerContext context, IpmiRAKPMessage4 message) {
            handleDefault(context, message);
        }

        @Override
        public void handleOemExplicit(IpmiHandlerContext context, OemExplicit message) {
            handleDefault(context, message);
        }

        @Override
        public void handleCommand(IpmiHandlerContext context, IpmiCommand message) {
            handleDefault(context, message);
        }

        @Override
        public void handleSOL(IpmiHandlerContext context, SOLMessage message) {
            handleDefault(context, message);
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
    public void handleOpenSessionRequest(@Nonnull IpmiHandlerContext context, @Nonnull IpmiOpenSessionRequest message);

    public void handleOpenSessionResponse(@Nonnull IpmiHandlerContext context, @Nonnull IpmiOpenSessionResponse message);

    public void handleRAKPMessage1(@Nonnull IpmiHandlerContext context, @Nonnull IpmiRAKPMessage1 message);

    public void handleRAKPMessage2(@Nonnull IpmiHandlerContext context, @Nonnull IpmiRAKPMessage2 message);

    public void handleRAKPMessage3(@Nonnull IpmiHandlerContext context, @Nonnull IpmiRAKPMessage3 message);

    public void handleRAKPMessage4(@Nonnull IpmiHandlerContext context, @Nonnull IpmiRAKPMessage4 message);

    public void handleOemExplicit(@Nonnull IpmiHandlerContext context, @Nonnull OemExplicit message);

    public void handleCommand(@Nonnull IpmiHandlerContext context, @Nonnull IpmiCommand message);

    public void handleSOL(@Nonnull IpmiHandlerContext context, @Nonnull SOLMessage message);
    // /* Does not distinguish between {@link IpmiPayloadType#OEM0} through {@link IpmiPayloadType#OEM7}. */
    // public void handleOEM(@Nonnull IpmiHandlerContext context,@Nonnull OEMMessage message)
}
