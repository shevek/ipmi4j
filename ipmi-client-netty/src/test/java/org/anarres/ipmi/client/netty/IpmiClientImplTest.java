/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.client.netty;

import org.anarres.ipmi.protocol.client.IpmiClient;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiContext;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiSessionManager;
import org.junit.Test;
// import static org.junit.Assert.*;

/**
 *
 * @author shevek
 */
public class IpmiClientImplTest {

    @Test
    public void testClient() {
        IpmiContext context = new IpmiSessionManager();
        IpmiClient client = new IpmiClientImpl(context);
    }
}