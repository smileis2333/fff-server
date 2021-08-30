package org.example.fff.server.util;

import org.example.fff.server.servlet.Session;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionRegistry {
    private Map<String, Session> sessions = new ConcurrentHashMap<>();

    public void addSession(Session session) {
        sessions.put(session.getId(), session);
    }

    public Session getSession(String sessionId) {
        Session session = sessions.getOrDefault(sessionId, null);

        if (session != null && session.getLastAccessTime().plusSeconds(session.getMaxInactiveInterval()).compareTo(LocalDateTime.now()) < 0) {
            // lazy evict
            evictSession(sessionId);
        }
        return session;
    }

    public void evictSession(String sessionId) {
        Session wtr = sessions.remove(sessionId);
        // todo，设置时间
    }

    public int getMaxInactiveInterval() {
        return 60;
    }
}
