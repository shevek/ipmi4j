/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.impl;

import com.google.common.primitives.Ints;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiLun;

/**
 * A value-object for an IPMI "connection" from a
 * source address/LUN to a target address/LUN.
 *
 * @author shevek
 */
public class IpmiConnection {

    private final byte localAddress;
    private final IpmiLun localLun;
    private final byte remoteAddress;
    private final IpmiLun remoteLun;

    public IpmiConnection(byte localAddress, IpmiLun localLun, byte remoteAddress, IpmiLun remoteLun) {
        this.localAddress = localAddress;
        this.localLun = localLun;
        this.remoteAddress = remoteAddress;
        this.remoteLun = remoteLun;
    }

    @Override
    public int hashCode() {
        return Ints.fromBytes(localAddress, localLun.getCode(), remoteAddress, remoteLun.getCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (null == obj)
            return false;
        if (!getClass().equals(obj.getClass()))
            return false;
        IpmiConnection o = (IpmiConnection) obj;
        return localAddress == o.localAddress
                && localLun == o.localLun
                && remoteAddress == o.remoteAddress
                && remoteLun == o.remoteLun;
    }
}
