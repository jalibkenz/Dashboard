package in.kenz.dashboard.servlet;

import in.kenz.dashboard.entity.User;
import in.kenz.dashboard.service.UserService;
import in.kenz.dashboard.service.impl.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String name = req.getParameter("name");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirmPassword");

        if (name == null || username == null || password == null || confirm == null) {
            resp.sendRedirect("signup.jsp?error=" + URLEncoder.encode("Please fill all fields", "UTF-8"));
            return;
        }

        if (!password.equals(confirm)) {
            resp.sendRedirect("signup.jsp?error=" + URLEncoder.encode("Passwords do not match", "UTF-8"));
            return;
        }

        if (password.length() < 6) {
            resp.sendRedirect("signup.jsp?error=" + URLEncoder.encode("Password too short", "UTF-8"));
            return;
        }

        if (userService.findByUserUsername(username) != null) {
            resp.sendRedirect("signup.jsp?error=" + URLEncoder.encode("Username already taken", "UTF-8"));
            return;
        }

        // CREATE USER
        User user = new User();// ← SAVE PLAIN PASSWORD
        user.setUserName(name);
        user.setUserUsername(username);
        user.setUserPassword(password);

        try {
            userService.saveUser(user);
            resp.sendRedirect("index.jsp?success=" + URLEncoder.encode("Account created. You can login now.", "UTF-8"));
        } catch (RuntimeException e) {
            resp.sendRedirect("signup.jsp?error=" + URLEncoder.encode("Server error", "UTF-8"));
        }
    }
}
