package vn.novahub.helpdesk.filter;

import org.springframework.web.filter.GenericFilterBean;
import vn.novahub.helpdesk.model.Request;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
public class TokenAuthenticationFilterBefore extends GenericFilterBean {

    private static ArrayList<Request> requestsNeedAuthencationToken;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String requestURI = httpServletRequest.getRequestURI();
        String method = httpServletRequest.getMethod();

        if (requestsNeedAuthencationToken == null)
            requestsNeedAuthencationToken = RequestsNeedAuthencationToken.get();

        boolean isCheckToken = false;

        for (Request request : requestsNeedAuthencationToken) {
            if (request.equal(requestURI, method)) {
                isCheckToken = true;
                break;
            }
        }

        if (isCheckToken) {
            httpServletRequest.setAttribute("url_request", requestURI);
            RequestDispatcher requestDispatcher = httpServletRequest.getServletContext().getRequestDispatcher("/api/authentication-token");
            requestDispatcher.forward(httpServletRequest, httpServletResponse);
            return;
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}