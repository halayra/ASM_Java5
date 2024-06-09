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
        // Kiểm tra xem người dùng đã đăng nhập chưa
        Object loggedInUser = session.get("loggedInUser");
        Object isAdmin = session.get("isAdmin");

        if (loggedInUser == null) {
            // Nếu chưa đăng nhập, chuyển hướng đến trang đăng nhập
            response.sendRedirect("/login");
            return false;
        } else if (!(boolean) isAdmin) {
            // Nếu không phải là admin, chuyển hướng hoặc trả về trang lỗi
            response.sendRedirect("/access-denied");
            return false;
        }

        // Nếu là admin đã đăng nhập, cho phép truy cập các đường dẫn dành cho manager
        return true;
    }
}


