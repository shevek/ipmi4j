/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.session;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public class IpmiSessionManager {

    private final SecureRandom random = new SecureRandom();
    private final IpmiSession dummy = new IpmiSession(0);
    private final ConcurrentMap<Integer, IpmiSession> sessions = new ConcurrentHashMap<>();

    @Nonnull
    public IpmiSession newSession() {
        for (;;) {
            int id = random.nextInt();
            if (sessions.putIfAbsent(id, dummy) == null) {
                IpmiSession session = new IpmiSession(id);
                sessions.put(id, session);
                return session;
            }
        }
    }

    @CheckForNull
    public IpmiSession getSession(int id) {
        return sessions.get(id);
    }
}
