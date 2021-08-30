package org.example.fff.server;

import org.example.fff.server.servlet.Request;
import org.example.fff.server.servlet.Response;
import org.example.fff.server.servlet.Session;
import org.example.fff.server.util.ConvertUtil;
import org.example.fff.server.util.SessionRegistry;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;

public class ServerConnector extends AbstractConnector {
    private Executor executor;

    public ServerConnector(int port) {
        super(port);
    }

    @Override
    public void accept() throws IOException {
        ServerSocket serverSocket = new ServerSocket(getPort());
        Socket income = null;
        while ((income = serverSocket.accept()) != null) {
            SessionRegistry sessionRegistry = getServer().getSessionRegistry();
            Request request = ConvertUtil.toRequest(income);
            request.setSessionRegistry(sessionRegistry);
            Cookie[] cookies = request.getCookies();

            boolean openSession = getServer().openSession();

            if (openSession) {
                String sessionId = null;
                for (Cookie cookie : cookies) {
                    if (getServer().getSessionIdName().equals(cookie.getName())) {
                        sessionId = cookie.getValue();
                        request.setSessionId(sessionId);
                        break;
                    }
                }
                if (sessionId != null) {
                    Session session = getServer().getSessionRegistry().getSession(sessionId);
                    request.setSession(session);
                }
            }


            Response response = ConvertUtil.toResponse(income);
            request.setResponse(response);
            getServer().getThreadPoolExecutor().execute(() -> {
                try {
                    getServer().getHandler().handler(request, response);
                } catch (ServletException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }
    }
}

