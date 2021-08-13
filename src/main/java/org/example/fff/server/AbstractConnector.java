package org.example.fff.server;

import java.util.concurrent.Executor;

public abstract class AbstractConnector extends LifeCycle.AbstractLifeCycle {
    private Server server;

    private int port;

    public AbstractConnector(int port) {
        this.port = port;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public void doStart() {
        getExecutor().execute(new Acceptor());
    }

    @Override
    public void doStop() {

    }

    public Executor getExecutor() {
        return server.getThreadPoolExecutor();
    }

    public Server getServer() {
        return server;
    }

    public int getPort() {
        return port;
    }

    public abstract void accept() throws Exception;

    /**
     * 分发请求
     */
    class Acceptor implements Runnable {
        private int id;

        @Override
        public void run() {
            // 分发io事件
            try {
                accept();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

