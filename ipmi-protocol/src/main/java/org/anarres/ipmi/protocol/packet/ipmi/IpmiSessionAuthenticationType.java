/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import com.google.common.primitives.UnsignedBytes;
import javax.annotation.Nonnegative;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.command.messaging.GetChannelAuthenticationCapabilitiesResponse;

/**
 * [IPMI2] Section 13.6 page 133, right margin of table.
 * NOT [IPMI2] Section 13.28 page 157.
 * Codes used as bit offsets in GetChannelAuthenticationCapabilitiesResponse.
 *
 * @see GetChannelAuthenticationCapabilitiesResponse
 * @author shevek
 */
public enum IpmiSessionAuthenticationType implements Code.Wrapper {

    /** This means IPMI v1.5 */
    NONE(0),
    /** This means IPMI v1.5 */
    MD2(1),
    /** This means IPMI v1.5 */
    MD5(2),
    /** This means IPMI v1.5 */
    PASSWORD(4),
    /** This means IPMI v1.5 */
    OEM_PROPRIETARY(5),
    /** This means IPMI v2.0 */
    RMCPP(6);
    private final byte code;

    private IpmiSessionAuthenticationType(@Nonnegative int code) {
        this.code = UnsignedBytes.checkedCast(code);
    }

    @Override
    public byte getCode() {
        return code;
    }
}
