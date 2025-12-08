package in.kenz.dashboard.servlet;

import in.kenz.dashboard.entity.User;
import in.kenz.dashboard.service.UserService;
import in.kenz.dashboard.service.impl.UserServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {

    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            String msg = URLEncoder.encode("Invalid username or password", "UTF-8");
            resp.sendRedirect("index.jsp?error=" + msg);
            return;
        }

        User user = userService.findByUserUsername(username);

        if (user == null || !user.getUserPassword().equals(password)) {
            String msg = URLEncoder.encode("Invalid username or password", "UTF-8");
            resp.sendRedirect("index.jsp?error=" + msg);
            return;
        }

        // SUCCESS → create session and show dashboard
        HttpSession session = req.getSession(true);
        //System.out.println("Role at login = " + user.getUserRole().getRoleName());
        session.setAttribute("loggedInUser", user);
        resp.sendRedirect("dashboard");


    }
}
