package codesquad.server.http.session;

public class SessionContext {

    private static final ThreadLocal<Session> CONTEXT = new ThreadLocal<>();

    private SessionContext() {
    }

    public static void setSession(Session session) {
        CONTEXT.set(session);
    }

    public static Session getSession() {
        return CONTEXT.get();
    }

    public static void removeSession() {
        CONTEXT.remove();
    }
}
