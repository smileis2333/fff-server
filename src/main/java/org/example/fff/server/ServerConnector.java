package org.example.fff.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;

/**
 * 作逻辑包裹和io分发
 */
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
            Request request = ConvertUtil.toRequest(income);
            Response response = ConvertUtil.toResponse(income);
            getServer().getThreadPoolExecutor().execute(() -> {
                getServer().getHandler().handler(request, response);
            });

        }
    }
}

