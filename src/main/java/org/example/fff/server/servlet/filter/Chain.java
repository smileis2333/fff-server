package org.example.fff.server.servlet.filter;

import org.example.fff.server.Server;

import javax.servlet.*;
import java.io.IOException;

public class Chain implements FilterChain {
    private Filter filter;
    private FilterChain nextChain;

    public Chain(Filter filter, FilterChain nextChain) {
        this.filter = filter;
        this.nextChain = nextChain;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        filter.doFilter(request, response, nextChain);
    }


    public static Chain newChain(Filter filter, FilterChain nextChain) {
        return new Chain(filter, nextChain);
    }

    public static ChainEnd newChainEnd(Servlet servlet) {
        return new ChainEnd(servlet);
    }
}
