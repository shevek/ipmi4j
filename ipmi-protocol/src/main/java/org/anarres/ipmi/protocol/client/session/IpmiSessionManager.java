/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.session;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public class IpmiSessionManager implements IpmiPacketContext {

    public static final SecureRandom RANDOM = new SecureRandom();
    // private final IpmiSession dummy = new IpmiSession(0);
    private final ConcurrentMap<Integer, IpmiSession> sessions = new ConcurrentHashMap<>();

    @Nonnull
    public IpmiSession newIpmiSession() {
        for (;;) {
            int id = RANDOM.nextInt();
            if (id == 0)
                continue;
            IpmiSession session = new IpmiSession(id);
            if (sessions.putIfAbsent(id, session) == null)
                return session;
        }
    }

    @Override
    public IpmiSession getIpmiSession(int id) {
        if (id == 0)
            return null;
        return sessions.get(id);
    }
}
