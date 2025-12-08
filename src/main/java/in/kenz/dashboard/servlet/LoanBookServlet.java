package in.kenz.dashboard.servlet;

import in.kenz.dashboard.entity.Loan;
import in.kenz.dashboard.entity.User;
import in.kenz.dashboard.service.LoanService;
import in.kenz.dashboard.service.impl.LoanServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@WebServlet("/loanBook")
public class LoanBookServlet extends HttpServlet {

    private final LoanService loanService = new LoanServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?error=" +
                    URLEncoder.encode("Please login first", StandardCharsets.UTF_8.name()));
            return;
        }

        User user = (User) session.getAttribute("loggedInUser");

        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendRedirect(req.getContextPath() + "/dashboard?error=" +
                    URLEncoder.encode("Invalid book", StandardCharsets.UTF_8.name()));
            return;
        }

        Long bookId;
        try {
            bookId = Long.parseLong(idParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/dashboard?error=" +
                    URLEncoder.encode("Invalid book ID", StandardCharsets.UTF_8.name()));
            return;
        }

        try {
            // Due date = 14 days
            LocalDate due = LocalDate.now().plusDays(14);
            Date dueDate = Date.from(due.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Create loan (validates availability)
            Loan loan = loanService.createLoan(user, bookId, dueDate);

            // Redirect to dashboard (PRG pattern)
            resp.sendRedirect(req.getContextPath() +
                    "/dashboard?success=" +
                    URLEncoder.encode("Book loaned successfully", StandardCharsets.UTF_8.name()));

        } catch (IllegalStateException ex) {
            resp.sendRedirect(req.getContextPath() + "/dashboard?error=" +
                    URLEncoder.encode(ex.getMessage(), StandardCharsets.UTF_8.name()));
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/dashboard?error=" +
                    URLEncoder.encode("Unable to loan book", StandardCharsets.UTF_8.name()));
        }
    }
}
