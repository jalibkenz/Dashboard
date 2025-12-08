package in.kenz.dashboard.servlet;

import in.kenz.dashboard.entity.Book;
import in.kenz.dashboard.entity.User;
import in.kenz.dashboard.service.BookService;
import in.kenz.dashboard.service.LoanService;
import in.kenz.dashboard.service.impl.BookServiceImpl;

import in.kenz.dashboard.service.impl.LoanServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final BookService bookService = new BookServiceImpl();
    private final LoanService loanService = new LoanServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        System.out.println("Dashboard Servlet is executed");
        // 1. Login check
        HttpSession session = req.getSession(false);
        User loggedInUser = (session != null)
                ? (User) session.getAttribute("loggedInUser")
                : null;

        if (loggedInUser == null) {
            // redirect to login page with message
            resp.sendRedirect(req.getContextPath()
                    + "/index.jsp?error=Please+login+first");
            return;
        }

        // 2. Load data using updated BookServiceImpl (Criteria-based)
        List<Book> loanedBooks;
        List<Book> availableBooks;

        try {

//            loanedBooks = bookService.findLoanedBooks();       // books currently loaned
//            availableBooks = bookService.findAvailableBooks(); // books not loaned
            loanedBooks = loanService.findLoanedBooksByUser(loggedInUser);
            availableBooks = bookService.findAvailableBooks();
            if (loanedBooks == null) loanedBooks = new ArrayList<>();
            if (availableBooks == null) availableBooks = new ArrayList<>();

        } catch (Exception e) {
            e.printStackTrace();

            // Fail-safe fallback so JSP loads
            loanedBooks = new ArrayList<>();

            // At least show all books if availableBooks query fails
            availableBooks = bookService.findAll();
            if (availableBooks == null) availableBooks = new ArrayList<>();
        }

        // 3. Supply data to JSP via request attributes (not session)
        req.setAttribute("loanedBooks", loanedBooks);
        req.setAttribute("availableBooks", availableBooks);

        // 4. Forward to dashboard JSP
        req.getRequestDispatcher("/dashboard.jsp").forward(req, resp);
    }

    // POST delegates to GET
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
