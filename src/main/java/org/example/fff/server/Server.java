package org.example.fff.server;

import org.example.fff.server.util.SessionRegistry;
import org.reflections.Reflections;

import javax.servlet.Filter;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.*;
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

        Set<Class> classes = new HashSet<>();
        if (scanPackages != null && !scanPackages.equals("")) {
            for (String scanPackage : scanPackages) {
                classes.addAll(getClasses(scanPackage));
            }
        }

        for (Class clazz : classes) {
            Annotation[] annotations = clazz.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == WebServlet.class) {
                    addServlet(clazz);
                } else if (annotation.annotationType() == WebFilter.class) {
                    addFilter(clazz);
                }
            }
        }

        for (AbstractConnector connector : connectors) {
            connector.start();
        }
    }

    private Set<Class> getClasses(String scanPackage) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String packagePath = scanPackage.replace(".", "/");
        try {
            Enumeration<URL> resources = classLoader.getResources(packagePath);
            Set<Class> classes = new HashSet<>();
            URL url = null;
            List<File> dirs = new ArrayList<>();
            while (resources.hasMoreElements() && (url = resources.nextElement()) != null) {
                dirs.add(new File(url.getFile()));
            }

            for (File dir : dirs) {
                classes.addAll(findClass(dir, scanPackage));
            }

            return classes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptySet();
    }

    private List<Class> findClass(File dir, String scanPackage) {
        ArrayList<Class> res = new ArrayList<>();

        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                res.addAll(findClass(file, scanPackage));
            }
        } else if (dir.isFile()) {
            if (dir.getName().contains(".class")) {
                String rp = dir.getAbsolutePath().replace("\\", ".");
                String className = rp.substring(rp.indexOf(scanPackage), rp.length() - 6);
                try {
                    res.add(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
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

    public Handler getHandler() {
        return handler;
    }

    public void addServlet(Class<HttpServlet> clazz) {
        handler.addServlet(clazz);
    }

    public void addFilter(Class<HttpFilter> clazz) {
        handler.addFilter(clazz);
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
