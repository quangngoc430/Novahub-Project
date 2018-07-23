package vn.novahub.helpdesk.config;

import org.springframework.web.filter.GenericFilterBean;
import vn.novahub.helpdesk.config.RequestsNeedAuthencationToken;
import vn.novahub.helpdesk.model.Request;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
public class TokenAuthenticationFilterBefore extends GenericFilterBean {

    private ArrayList<Request> requestsNeedAuthencationToken;

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

            RequestDispatcher requestDispatcher;
            switch (method){
                case "GET" : {
                    requestDispatcher = httpServletRequest.getServletContext().getRequestDispatcher("/api/authentication-token");
                    break;
                }
                case "POST" : {
                    requestDispatcher = httpServletRequest.getServletContext().getRequestDispatcher("/api/authentication-token-post");
                    break;
                }
                case "PUT" : {
                    requestDispatcher = httpServletRequest.getServletContext().getRequestDispatcher("/api/authentication-token-put");
                    break;
                }
                default: { //DELETE
                    requestDispatcher = httpServletRequest.getServletContext().getRequestDispatcher("/api/authentication-token-delete");
                    break;
                }
            }
            requestDispatcher.forward(httpServletRequest, httpServletResponse);
            return;
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}