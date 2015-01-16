/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCompletionCode;

/**
 *
 * @author shevek
 */
public abstract class AbstractIpmiResponse extends AbstractIpmiCommand implements IpmiResponse {

    private byte ipmiCompletionCode;

    // TODO: Refactor generally into getDataWireLength().
    protected boolean isIpmiCompletionCodeSuccess() {
        return ipmiCompletionCode == 0;
    }

    public byte getIpmiCompletionCode() {
        return ipmiCompletionCode;
    }

    public void setIpmiCompletionCode(byte ipmiCompletionCode) {
        this.ipmiCompletionCode = ipmiCompletionCode;
    }

    /** Returns true if the completion code is not a success. */
    protected boolean toWireCompletionCode(@Nonnull ByteBuffer buffer) {
        byte code = getIpmiCompletionCode();
        buffer.put(code);
        return code != 0;
    }

    /** Returns true if the completion code is not a success. */
    protected boolean fromWireCompletionCode(@Nonnull ByteBuffer buffer) {
        byte code = buffer.get();
        setIpmiCompletionCode(code);
        return code != 0;
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        byte code = getIpmiCompletionCode();
        try {
            IpmiCompletionCode data = Code.fromByte(IpmiCompletionCode.class, code);
            appendValue(buf, depth, "IpmiCompletionCode", data);
        } catch (IllegalArgumentException e) {
            appendValue(buf, depth, "IpmiCompletionCode", "0x" + UnsignedBytes.toString(code, 16));
        }
    }
}
