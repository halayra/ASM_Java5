package org.phamxuantruong.asm2.Controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.phamxuantruong.asm2.Entity.PasswordResetToken;
import org.phamxuantruong.asm2.Entity.Users;
import org.phamxuantruong.asm2.Interface.PasswordResetTokenDAO;
import org.phamxuantruong.asm2.Interface.UsersDAO;
import org.phamxuantruong.asm2.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
public class LoginController {
    @Autowired
    UsersDAO dao;
    @Autowired
    PasswordResetTokenDAO tokenDao;
    @Autowired
    EmailService emailService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/user/login")
    public String login(Model model) {
        model.addAttribute("user", new Users());
        return "home/login";
    }

    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String login(Model model, @RequestParam(name = "username", required = false) String username,
                        @RequestParam(name = "password", required = false) String password, HttpSession session) {
        if (username != null && !username.isEmpty()) {
            // Tìm kiếm trong cơ sở dữ liệu
            Optional<Users> userOptional = dao.findByUsername(username);

            if (userOptional.isPresent()) {
                Users user = userOptional.get();
                if (passwordEncoder.matches(password, user.getPassword())) {
                    // Đăng nhập thành công
                    session.setAttribute("loggedInUser", user.getUsername()); // Lưu tên người dùng vào session
                    return "redirect:/";
                } else {
                    // Sai mật khẩu, hiển thị thông báo lỗi
                    model.addAttribute("error", "Sai mật khẩu hoặc tên đăng nhập");
                }
            } else {
                // Người dùng không tồn tại, hiển thị thông báo lỗi
                model.addAttribute("error", "Sai mật khẩu hoặc tên đăng nhập");
            }
        } else {
            // Username không được cung cấp, hiển thị thông báo lỗi
            model.addAttribute("error", "Vui lòng nhập tên đăng nhập");
        }

        model.addAttribute("user", new Users());
        return "home/login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new Users());
        return "home/signup";
    }

    @PostMapping("/signup/save")
    public String saveUser(@Valid @ModelAttribute("user") Users user, BindingResult bindingResult, Model model) {
        Optional<Users> existingPhone = dao.findByPhoneNumber(user.getPhoneNumber());
        if (existingPhone.isPresent()) {
            bindingResult.rejectValue("phoneNumber", "error.user", "Số điện thoại đã tồn tại trong hệ thống.");
        }

        if (dao.findByEmail(user.getEmail()) != null) {
            bindingResult.rejectValue("email", "error.user", "Địa chỉ email đã tồn tại trong hệ thống.");
        }

        Optional<Users> existingUser = dao.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            bindingResult.rejectValue("username", "error.user", "Tên người dùng đã tồn tại trong hệ thống.");
        }

        // Kiểm tra mật khẩu nhập lại
        if (!user.getPassword().equals(user.getRepeatPassword())) {
            bindingResult.rejectValue("repeatPassword", "error.user", "Mật khẩu nhập lại không khớp.");
        }

        // In ra thông tin lỗi nếu có
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println("Error: " + error.getDefaultMessage());
            });
            return "home/signup";
        }

        // Mã hóa mật khẩu trước khi lưu
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // In ra thông tin người dùng để kiểm tra
        System.out.println("User: " + user);

        // Lưu người dùng
        dao.save(user);
        System.out.println("User saved successfully");

        return "redirect:/user/login";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email, Model model) {
        // Tìm người dùng theo email
        Users user = dao.findByEmail(email);
        if (user != null) {
            // Tạo token duy nhất
            String token = UUID.randomUUID().toString();
            // Lưu token vào cơ sở dữ liệu với thời gian hết hạn (tùy chọn)
            PasswordResetToken resetToken = new PasswordResetToken(token, user, LocalDateTime.now().plusHours(1));
            tokenDao.save(resetToken);

            // Gửi email
            emailService.sendPasswordResetEmail(user.getEmail(), token);

            model.addAttribute("message", "Liên kết đặt lại mật khẩu đã được gửi tới email của bạn.");
        } else {
            model.addAttribute("error", "Không tìm thấy người dùng với email này.");
        }
        return "home/forgotPass";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token,
                                @RequestParam("password") String password,
                                Model model) {
        // Tìm token trong cơ sở dữ liệu
        PasswordResetToken resetToken = tokenDao.findByToken(token);
        if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Token đặt lại mật khẩu không hợp lệ hoặc đã hết hạn.");
            return "home/forgotPass";
        }

        // Cập nhật mật khẩu cho người dùng
        Users user = resetToken.getUser();

         //Mã hóa mật khẩu trước khi lưu
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);

        // Lưu người dùng với mật khẩu mới
        dao.save(user);

        // Xóa hoặc vô hiệu hóa token sau khi sử dụng
        tokenDao.delete(resetToken);

        model.addAttribute("message", "Đặt lại mật khẩu thành công.");
        return "redirect:/user/login";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Xóa tất cả dữ liệu trong session
        return "redirect:/user/login"; // Chuyển hướng về trang đăng nhập
    }
}
