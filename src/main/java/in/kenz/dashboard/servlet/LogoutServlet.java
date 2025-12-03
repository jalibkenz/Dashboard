package in.kenz.dashboard.servlet;

import in.kenz.dashboard.entity.Book;
import in.kenz.dashboard.entity.Loan;
import in.kenz.dashboard.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        List<Book> loanedBooks = null;

        if (session != null) {
            loanedBooks = (List<Book>) session.getAttribute("loanedBooks");
            session.invalidate();  // logout
        }

        // Pass books to JSP
        req.setAttribute("loanedBooks", loanedBooks);

        req.getRequestDispatcher("/logout.jsp").forward(req, resp);
    }
}
