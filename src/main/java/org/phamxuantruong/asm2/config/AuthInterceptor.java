package org.phamxuantruong.asm2.config;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.phamxuantruong.asm2.Entity.Users;
import org.phamxuantruong.asm2.Service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    SessionService session;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Users user = session.get("user");
        if (user == null || !user.getAdmin()) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}

