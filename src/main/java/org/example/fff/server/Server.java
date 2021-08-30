package org.example.fff.server;

import org.example.fff.server.util.SessionRegistry;

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
    private SessionRegistry sessionRegistry = new SessionRegistry();
    private boolean openSession;
    private String sessionIdName = "sessionId";
    private String[] scanPackages;

    public void setConnectors(AbstractConnector[] connectors) {
        this.connectors = connectors;
    }


    public String[] getScanPackages() {
        return scanPackages;
    }

    public void setScanPackages(String[] scanPackages) {
        this.scanPackages = scanPackages;
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

    public SessionRegistry getSessionRegistry() {
        return sessionRegistry;
    }

    public boolean openSession() {
        return openSession;
    }

    public void setOpenSession(boolean openSession) {
        this.openSession = openSession;
    }

    public String getSessionIdName() {
        return sessionIdName;
    }

    public void setSessionIdName(String sessionIdName) {
        this.sessionIdName = sessionIdName;
    }
}
