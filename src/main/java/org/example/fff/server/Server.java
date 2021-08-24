package org.example.fff.server;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 大配置，总启动器
 */
public class Server extends LifeCycle.AbstractLifeCycle {
    private AbstractConnector[] connectors;
    private Executor threadPoolExecutor = Executors.newFixedThreadPool(5);
    private SimpleHandler handler = new SimpleHandler();

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

    public Executor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public Handler getHandler() {
        return handler;
    }

    public void addServlet(String path, HttpServlet servlet) {
        handler.addServlet(servlet);
        handler.addMapping(path, servlet.getServletName());
    }
}
