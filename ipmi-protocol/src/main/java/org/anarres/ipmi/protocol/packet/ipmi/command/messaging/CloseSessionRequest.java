/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.messaging;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.session.IpmiSession;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiCommandHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiRequest;

/**
 * [IPMI2] Section 22.19, table 22-24, page 297.
 *
 * @author shevek
 */
public class CloseSessionRequest extends AbstractIpmiRequest {

    public int sessionId;
    public byte sessionHandle;

    public CloseSessionRequest() {
    }

    public CloseSessionRequest(int sessionId) {
        this.sessionId = sessionId;
    }

    public CloseSessionRequest(@Nonnull IpmiSession session) {
        this(session.getConsoleSessionId());
    }

    public CloseSessionRequest withSessionId(int sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public CloseSessionRequest withSessionHandle(byte sessionHandle) {
        this.sessionHandle = sessionHandle;
        return this;
    }

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.CloseSession;
    }

    @Override
    public void apply(IpmiClientIpmiCommandHandler handler, IpmiHandlerContext context) {
        handler.handleCloseSessionRequest(context, this);
    }

    @Override
    public int getDataWireLength() {
        return (sessionId == 0) ? 5 : 4;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        toWireIntLE(buffer, sessionId);
        if (sessionId == 0)
            buffer.put(sessionHandle);
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        sessionId = fromWireIntLE(buffer);
        if (sessionId == 0)
            sessionHandle = buffer.get();
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "SessionId", "0x" + Integer.toHexString(sessionId));
        appendValue(buf, depth, "SessionHandle", "0x" + UnsignedBytes.toString(sessionHandle, 16));
    }
}
