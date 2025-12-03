package in.kenz.dashboard.servlet;

import in.kenz.dashboard.entity.Book;
import in.kenz.dashboard.entity.User;

import in.kenz.dashboard.service.BookService;
import in.kenz.dashboard.service.impl.BookServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final BookService bookService = new BookServiceImpl();

    // Simple test servlet: always forwards empty lists so the JSP renders
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // require login (same check your JSP expects)
        HttpSession session = req.getSession(false);
        User loggedIn = (session != null) ? (User) session.getAttribute("loggedInUser") : null;
        if (loggedIn == null) {
            resp.sendRedirect("index.jsp?error=Please+login+first");
            return;
        }

        // Provide empty lists so dashboard.jsp renders fine
        List<Book> loanedBooks;
        List<Book> availableBooks;

        try {
            // these methods must be implemented in BookService / BookServiceImpl
            loanedBooks = bookService.findLoanedBooks();
            availableBooks = bookService.findAvailableBooks();
            if (loanedBooks == null) loanedBooks = new ArrayList<>();
            if (availableBooks == null) availableBooks = new ArrayList<>();
        } catch (Exception e) {
            // fail-safe: log and fallback so JSP still renders
            e.printStackTrace();
            loanedBooks = new ArrayList<>();
            availableBooks = bookService.findAll(); // best-effort: show all books if available
            if (availableBooks == null) availableBooks = new ArrayList<>();
        }

        req.setAttribute("loanedBooks", loanedBooks);
        req.setAttribute("availableBooks", availableBooks);
        req.getRequestDispatcher("dashboard.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // treat POST same as GET for convenience
        doGet(req, resp);
    }
}
