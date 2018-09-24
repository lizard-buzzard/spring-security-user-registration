package com.lizard.buzzard.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component("authenticationSuccessHandler")
@ComponentScan(basePackages = {"com.avicomp.avida.main.config"})
public class MyCustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final Logger logger = LoggerFactory.getLogger(MyCustomAuthenticationSuccessHandler.class);

    @Value("${lizard.session.max.inactive.interval}")
    private Integer sessionMaxInactiveInterval;

    @Autowired
    CustomRememberMeServices rememberMeServices;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication auth) throws IOException, ServletException {

        // TODO: set targetUrl depending on user/admin roles
        String targetUrl = "/homepage.html?user=" + auth.getName();

        redirectStrategy.sendRedirect(request, response, targetUrl);

        final HttpSession session = request.getSession(false);
        if (session != null) {
            // inactive interval in minutes between client requests before the servlet container will invalidate this session
            session.setMaxInactiveInterval(sessionMaxInactiveInterval * 60);
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

//            rememberMeServices.loginSuccess(request, response, auth);     // experimental, doesn't work for me
            // NOTE: uncomment in case if SecurityConfig.configure(HttpSecurity).rememberMeServices(rememberMeServices).key("theKey");
//            rememberMeServices.onLoginSuccess(request, response, auth);
        }
    }
}
