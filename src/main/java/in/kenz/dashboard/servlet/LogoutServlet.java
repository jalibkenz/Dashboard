package in.kenz.dashboard.servlet;

import in.kenz.dashboard.entity.Book;
import in.kenz.dashboard.entity.User;
import in.kenz.dashboard.service.LoanService;
import in.kenz.dashboard.service.impl.LoanServiceImpl;
import in.kenz.dashboard.util.EntityManagerFactoryProvider;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private final LoanService loanService = new LoanServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        User loggedIn = null;
        List<Book> loanedBooks = new ArrayList<>();

        // Step 1 — Read loggedInUser BEFORE invalidating session
        if (session != null) {
            Object u = session.getAttribute("loggedInUser");
            if (u instanceof User) loggedIn = (User) u;
        }

        // Step 2 — Re-fetch fresh User from DB
        if (loggedIn != null && loggedIn.getUserId() != null) {
            EntityManager em = EntityManagerFactoryProvider.getEntityManager();
            try {
                loggedIn = em.find(User.class, loggedIn.getUserId());
            } finally {
                em.close();
            }
        }

        // Step 3 — Fetch user’s loaned books
        if (loggedIn != null && loggedIn.getUserId() != null) {
            loanedBooks = loanService.findLoanedBooksByUser(loggedIn);
            if (loanedBooks == null) loanedBooks = new ArrayList<>();
        }

        // Step 4 — Invalidate session (AFTER collecting data)
        try {
            req.logout();  // Also invalidates session + security context
            if (session != null) {
                System.out.println("1SESSION EXIST-------------->");
                session.invalidate();
                System.out.println("1SESSION EXIST SOOOOO INVALIDATED-------------->");
            }
            System.out.println("Logout------[req.logout(); executed]-------------->");
        } catch (ServletException e) {
            if (session != null) {
                System.out.println("2SESSION EXIST-------------->");
                session.invalidate();
                System.out.println("2SESSION EXIST SOOOOO INVALIDATED-------------->");
            }
        }

        // Step 5 — Pass list to JSP
        req.setAttribute("loanedBooks", loanedBooks);
        req.getRequestDispatcher("/logout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}