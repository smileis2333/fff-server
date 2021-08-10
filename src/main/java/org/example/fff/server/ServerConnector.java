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
            Socket finalIncome = income;
            getServer().getThreadPoolExecutor().execute(() -> {

                getServer().getHandler().handler(ConvertUtil.toRequest(finalIncome), ConvertUtil.toResponse(finalIncome));
            });

        }
    }
}

class ConvertUtil {
    public static Request toRequest(Socket income) {
        return new Request();
    }

    public static Response toResponse(Socket income) {
        return new Response();
    }
}