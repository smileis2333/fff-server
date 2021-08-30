package org.example.fff.server.servlet.filter;

import org.example.fff.server.servlet.Request;
import org.example.fff.server.servlet.Response;
import org.example.fff.server.util.ResponseHeaderWriter;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

public class ChainEnd implements FilterChain {
    private Servlet servlet;

    public ChainEnd(Servlet servlet) {
        this.servlet = servlet;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        try {
            servlet.service(request, response);
            ((Request)request).refreshSession();
            ResponseHeaderWriter headerWriter = ((Response)response).getHeaderWriter();
            if (!headerWriter.isCommit()) {
                headerWriter.printHeader();
            }
            PrintWriter writer = response.getWriter();
            writer.flush();
            ((Request)request).getIncome().close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            System.out.println("servlet处理异常");
        }
    }
}
