/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.visitor;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.session.IpmiSession;
import org.anarres.ipmi.protocol.packet.ipmi.command.IpmiCommand;
import org.anarres.ipmi.protocol.packet.ipmi.payload.AbstractTaggedIpmiPayload;
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

    public static class TaggedAdapter extends Adapter {

        protected void handleTagged(@Nonnull IpmiHandlerContext context, @CheckForNull IpmiSession session, @Nonnull AbstractTaggedIpmiPayload message) {
            handleDefault(context, session, message);
        }

        @Override
        public void handleOpenSessionRequest(IpmiHandlerContext context, IpmiSession session, IpmiOpenSessionRequest message) {
            handleTagged(context, session, message);
        }

        @Override
        public void handleOpenSessionResponse(IpmiHandlerContext context, IpmiSession session, IpmiOpenSessionResponse message) {
            handleTagged(context, session, message);
        }

        @Override
        public void handleRAKPMessage1(IpmiHandlerContext context, IpmiSession session, IpmiRAKPMessage1 message) {
            handleTagged(context, session, message);
        }

        @Override
        public void handleRAKPMessage2(IpmiHandlerContext context, IpmiSession session, IpmiRAKPMessage2 message) {
            handleTagged(context, session, message);
        }

        @Override
        public void handleRAKPMessage3(IpmiHandlerContext context, IpmiSession session, IpmiRAKPMessage3 message) {
            handleTagged(context, session, message);
        }

        @Override
        public void handleRAKPMessage4(IpmiHandlerContext context, IpmiSession session, IpmiRAKPMessage4 message) {
            handleTagged(context, session, message);
        }
    }

    public static class Adapter implements IpmiClientIpmiPayloadHandler {

        protected void handleDefault(@Nonnull IpmiHandlerContext context, @CheckForNull IpmiSession session, @Nonnull IpmiPayload payload) {
        }

        @Override
        public void handleOpenSessionRequest(IpmiHandlerContext context, IpmiSession session, IpmiOpenSessionRequest message) {
            handleDefault(context, session, message);
        }

        @Override
        public void handleOpenSessionResponse(IpmiHandlerContext context, IpmiSession session, IpmiOpenSessionResponse message) {
            handleDefault(context, session, message);
        }

        @Override
        public void handleRAKPMessage1(IpmiHandlerContext context, IpmiSession session, IpmiRAKPMessage1 message) {
            handleDefault(context, session, message);
        }

        @Override
        public void handleRAKPMessage2(IpmiHandlerContext context, IpmiSession session, IpmiRAKPMessage2 message) {
            handleDefault(context, session, message);
        }

        @Override
        public void handleRAKPMessage3(IpmiHandlerContext context, IpmiSession session, IpmiRAKPMessage3 message) {
            handleDefault(context, session, message);
        }

        @Override
        public void handleRAKPMessage4(IpmiHandlerContext context, IpmiSession session, IpmiRAKPMessage4 message) {
            handleDefault(context, session, message);
        }

        @Override
        public void handleCommand(IpmiHandlerContext context, IpmiSession session, IpmiCommand message) {
            handleDefault(context, session, message);
        }

        @Override
        public void handleSOL(IpmiHandlerContext context, IpmiSession session, SOLMessage message) {
            handleDefault(context, session, message);
        }

        @Override
        public void handleOemExplicit(IpmiHandlerContext context, IpmiSession session, OemExplicit message) {
            handleDefault(context, session, message);
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
    public void handleOpenSessionRequest(@Nonnull IpmiHandlerContext context, @CheckForNull IpmiSession session, @Nonnull IpmiOpenSessionRequest message);

    public void handleOpenSessionResponse(@Nonnull IpmiHandlerContext context, @CheckForNull IpmiSession session, @Nonnull IpmiOpenSessionResponse message);

    public void handleRAKPMessage1(@Nonnull IpmiHandlerContext context, @CheckForNull IpmiSession session, @Nonnull IpmiRAKPMessage1 message);

    public void handleRAKPMessage2(@Nonnull IpmiHandlerContext context, @CheckForNull IpmiSession session, @Nonnull IpmiRAKPMessage2 message);

    public void handleRAKPMessage3(@Nonnull IpmiHandlerContext context, @CheckForNull IpmiSession session, @Nonnull IpmiRAKPMessage3 message);

    public void handleRAKPMessage4(@Nonnull IpmiHandlerContext context, @CheckForNull IpmiSession session, @Nonnull IpmiRAKPMessage4 message);

    public void handleCommand(@Nonnull IpmiHandlerContext context, @CheckForNull IpmiSession session, @Nonnull IpmiCommand message);

    public void handleSOL(@Nonnull IpmiHandlerContext context, @CheckForNull IpmiSession session, @Nonnull SOLMessage message);

    public void handleOemExplicit(@Nonnull IpmiHandlerContext context, @CheckForNull IpmiSession session, @Nonnull OemExplicit message);
    // /* Does not distinguish between {@link IpmiPayloadType#OEM0} through {@link IpmiPayloadType#OEM7}. */
    // public void handleOEM(@Nonnull IpmiHandlerContext context, @CheckForNull IpmiSession session, @Nonnull OEMMessage message)
}
