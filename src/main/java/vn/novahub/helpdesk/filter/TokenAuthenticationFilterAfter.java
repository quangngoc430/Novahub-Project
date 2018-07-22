package vn.novahub.helpdesk.filter;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import vn.novahub.helpdesk.model.Request;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
public class TokenAuthenticationFilterAfter extends GenericFilterBean {

    private ArrayList<Request> requestsNeedAuthencationToken;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String requestURI = httpServletRequest.getRequestURI();
        String method = httpServletRequest.getMethod();

        if (requestsNeedAuthencationToken == null)
            requestsNeedAuthencationToken = RequestsNeedAuthencationToken.get();

        for (Request request : requestsNeedAuthencationToken) {
            if (request.equal(requestURI, method)) {
                SecurityContextHolder.clearContext();
                break;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}