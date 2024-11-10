package com.example.training.handler;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Xuxinyuan
 */
@Component
@WebFilter(urlPatterns = "/*", filterName = "SQLInjection", initParams = { @WebInitParam(name = "regex",
        value = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|(\\b(and|exec|execute|insert|select|delete|update|count|" +
                "drop|%|chr|mid|master|truncate|char|declare|sitename|net user|xp_cmdshell|or|like'|and|exec|" +
                "execute|insert|create|drop|table|from|grant|use|group_concat|column_name|information_schema.columns|" +
                "table_schema|union|where|select|delete|update|order|by|count|chr|mid|master|truncate|char|declare|or|" +
                "--|like)\\b)") })
public class SqlInjectFilter implements Filter {
    private static final Logger logger = Logger.getLogger(SqlInjectFilter.class);
    private String regx;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.regx = filterConfig.getInitParameter("regex");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        Map<String, String[]> parametersMap = servletRequest.getParameterMap();
        for (Map.Entry<String, String[]> stringEntry : parametersMap.entrySet()) {
            String[] value = (String[]) stringEntry.getValue();
            for (String s : value) {
                if (null != s && this.regx != null) {
                    Pattern p = Pattern.compile(this.regx);
                    Matcher m = p.matcher(s);
                    if (m.find()) {
                        logger.error("您输入的参数有非法字符，请输入正确的参数！");
                        servletRequest.setAttribute("err", "您输入的参数有非法字符，请输入正确的参数！");
                        servletRequest.setAttribute("pageUrl", req.getRequestURI());
                        servletRequest.getRequestDispatcher("/error").forward(servletRequest, servletResponse);
                        return;
                    }
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
