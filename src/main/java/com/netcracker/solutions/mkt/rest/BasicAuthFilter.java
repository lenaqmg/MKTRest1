package com.netcracker.solutions.mkt.rest;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BasicAuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;


        String authHeader = httpReq.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring("Basic ".length());
            byte[] credBytes = org.apache.commons.codec.binary.Base64.decodeBase64(base64Credentials.getBytes("UTF-8"));
            String credentials = new String(credBytes, "UTF-8");
            String[] values = credentials.split(":", 2);
            if (values.length == 2 /*&& USERNAME.equals(values[0])*/ /*&& PASSWORD.equals(hashed)*/) {
                httpReq.setAttribute("authenticatedUser", values[0]);
                chain.doFilter(request, response);
                return;
            }
        }

        // если не авторизован
        httpResp.setHeader("WWW-Authenticate", "Basic realm=\"MyRESTApp\"");
        httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    @Override
    public void destroy() { }
}
