/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientRmcpMessageHandler;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiCommand;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiOpenSessionRequest;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiOpenSessionResponse;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayloadType;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiRAKPMessage1;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiRAKPMessage2;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiRAKPMessage3;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiRAKPMessage4;
import org.anarres.ipmi.protocol.packet.ipmi.payload.SOLMessage;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiIntegrityAlgorithm;
import org.anarres.ipmi.protocol.client.session.IpmiContext;
import org.anarres.ipmi.protocol.client.session.IpmiSession;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.ipmi.payload.OemExplicit;
import org.anarres.ipmi.protocol.packet.rmcp.RmcpMessageClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author shevek
 */
public abstract class AbstractIpmiSessionWrapper extends AbstractWireable implements IpmiSessionWrapper {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractIpmiSessionWrapper.class);

    /**
     * @see AbstractIpmiCommand#fromWireUnchecked(IpmiContext, ByteBuffer)
     */
    @Nonnull
    protected static IpmiPayload newPayload(@Nonnull ByteBuffer buffer, @Nonnull IpmiPayloadType payloadType) {
        switch (payloadType) {
            case RMCPOpenSessionRequest:
                return new IpmiOpenSessionRequest();
            case RMCPOpenSessionResponse:
                return new IpmiOpenSessionResponse();
            case RAKPMessage1:
                return new IpmiRAKPMessage1();
            case RAKPMessage2:
                return new IpmiRAKPMessage2();
            case RAKPMessage3:
                return new IpmiRAKPMessage3();
            case RAKPMessage4:
                return new IpmiRAKPMessage4();
            case IPMI:
                int position = buffer.position();
                int networkFunctionByte = UnsignedBytes.toInt(buffer.get(position + 1));
                // LOG.info("NetworkFunctionByte is 0x" + Integer.toHexString(networkFunctionByte));
                IpmiNetworkFunction networkFunction = Code.fromInt(IpmiNetworkFunction.class, (networkFunctionByte >>> 2) & ~1);
                byte commandNameByte = buffer.get(position + 5);
                IpmiCommandName commandName = IpmiCommandName.fromByte(networkFunction, commandNameByte);
                boolean isResponse = ((networkFunctionByte >>> 2) & 1) != 0;
                return isResponse ? commandName.newResponseMessage() : commandName.newRequestMessage();
            case SOL:
                return new SOLMessage();
            case OEM_EXPLICIT:
                return new OemExplicit();
            default:
                throw new UnsupportedOperationException("Unsupported payload type " + payloadType);
        }
    }
    private int ipmiSessionId;
    private int ipmiSessionSequenceNumber;
    private IpmiPayload ipmiPayload;

    @Override
    public RmcpMessageClass getMessageClass() {
        return RmcpMessageClass.IPMI;
    }

    @Override
    public int getIpmiSessionId() {
        return ipmiSessionId;
    }

    @Override
    public void setIpmiSessionId(int ipmiSessionId) {
        this.ipmiSessionId = ipmiSessionId;
    }

    /*
    @Nonnull
    protected IpmiSession getIpmiSession(@Nonnull IpmiContext context) {
        int sessionId = getIpmiSessionId();
        if (sessionId == 0)
            throw new IllegalStateException("Cannot lookup illegal IPMI session id 0.");
        IpmiSession session = context.getIpmiSession(sessionId);
        if (session == null)
            throw new IllegalStateException("No such IPMI session " + Integer.toHexString(sessionId));
        return session;
    }
    */

    @Override
    public int getIpmiSessionSequenceNumber() {
        return ipmiSessionSequenceNumber;
    }

    @Override
    public void setIpmiSessionSequenceNumber(int ipmiSessionSequenceNumber) {
        this.ipmiSessionSequenceNumber = ipmiSessionSequenceNumber;
    }

    @Override
    public IpmiPayload getIpmiPayload() {
        return ipmiPayload;
    }

    @Override
    public void setIpmiPayload(@Nonnull IpmiPayload ipmiPayload) {
        this.ipmiPayload = ipmiPayload;
    }

    @Override
    public void apply(IpmiClientRmcpMessageHandler handler, IpmiHandlerContext context) {
        handler.handleIpmiRmcpData(context, this);
    }

    /*
    @Override
    public void apply(IpmiClientIpmiPayloadHandler handler, IpmiHandlerContext context) {
        getIpmiPayload().apply(handler, context, this);
    }
    */

    @Nonnull
    public static IpmiIntegrityAlgorithm getIntegrityAlgorithm(@CheckForNull IpmiSession session) {
        if (session == null)
            return IpmiIntegrityAlgorithm.NONE;
        return session.getIntegrityAlgorithm();
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        appendHeader(buf, depth, "IpmiSessionData");
        depth++;
        appendValue(buf, depth, "SessionId", "0x" + Integer.toHexString(getIpmiSessionId()));
        appendValue(buf, depth, "SessionSequenceNumber", "0x" + Integer.toHexString(getIpmiSessionSequenceNumber()));
        appendChild(buf, depth, "IpmiPayload", getIpmiPayload());
    }
}
