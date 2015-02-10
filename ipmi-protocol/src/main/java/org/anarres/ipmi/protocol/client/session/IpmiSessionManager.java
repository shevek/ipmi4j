/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.session;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public class IpmiSessionManager implements IpmiContext {

    private final SecureRandom random = new SecureRandom();
    private final IpmiSession dummy = new IpmiSession(this, 0);
    private final ConcurrentMap<Integer, IpmiSession> sessions = new ConcurrentHashMap<>();

    @Nonnull
    public Random getRandom() {
        return random;
    }

    @Nonnull
    public IpmiSession newIpmiSession() {
        for (;;) {
            int id = random.nextInt();
            if (id == 0)
                continue;
            IpmiSession session = new IpmiSession(this, id);
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
