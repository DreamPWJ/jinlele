package com.jinlele.util.weixinUtils.util;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 浏览器过滤器（限制只能通过微信浏览器才能访问）
 */
public class BrowerFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        String userAgent = req.getHeader("User-Agent");
        if(userAgent.contains("MicroMessenger")){
            chain.doFilter(req ,resp);
        }else{
            out.println("请使用微信浏览器访问");
            out.close();
        }
    }

    @Override
    public void destroy() {

    }
}
