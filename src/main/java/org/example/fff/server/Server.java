package org.example.fff.server;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 大配置，总启动器
 */
public class Server extends LifeCycle.AbstractLifeCycle {
    private AbstractConnector[] connectors;
    private ThreadPoolExecutor threadPoolExecutor;
    private Handler handler;

    public void setConnectors(AbstractConnector[] connectors) {
        this.connectors = connectors;
    }

    @Override
    public void doStart() {
        if (connectors == null || connectors.length == 0) {
            // 启动失败
        }

        for (AbstractConnector connector : connectors) {
            connector.start();
        }
    }

    @Override
    public void doStop() {
        if (connectors != null)
            for (AbstractConnector connector : connectors) {
                connector.stop();
            }
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public Handler getHandler() {
        return handler;
    }
}
