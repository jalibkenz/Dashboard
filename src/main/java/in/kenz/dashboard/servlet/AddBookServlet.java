package in.kenz.dashboard.servlet;

import in.kenz.dashboard.entity.Book;
import in.kenz.dashboard.entity.User;
import in.kenz.dashboard.service.BookService;
import in.kenz.dashboard.service.impl.BookServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;

@WebServlet("/addBook")
public class AddBookServlet extends HttpServlet {

    private final BookService bookService = new BookServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        //Upon login session.setAttribute("loggedInUser", user);

        // 1. Ensure session exists (user logged in)
        HttpSession session = req.getSession(false);
        User inUser = (session != null) ? (User) session.getAttribute("loggedInUser") : null;

        if (inUser == null) {
            resp.sendRedirect("index.jsp?error=" + URLEncoder.encode("Please login first", "UTF-8"));
            return;
        }

        // 2. Read form values
        String title = req.getParameter("title");
        String author = req.getParameter("author");
        String category = req.getParameter("category");

        //SANITATION
        //Easing category to left blank and later identified
        if (category == null) category = ""; // avoid nulls, service may validate later

        // 3. Create Book entity
        Book book = new Book();
        book.setBookName(title != null ? title.trim() : "");
        book.setBookAuthor(author != null ? author.trim() : "");
        book.setBookCategory(category.trim());

        try {
            // 4. Business validation + save
            bookService.save(book);

            // 5. Redirect back to dashboard
            resp.sendRedirect("dashboard");

        } catch (IllegalArgumentException e) {
            // validation problem from service layer
            resp.sendRedirect("dashboard?error=" + URLEncoder.encode(e.getMessage(), "UTF-8"));

        } catch (RuntimeException e) {
            // DB error / system error
            e.printStackTrace();
            resp.sendRedirect("dashboard?error=" + URLEncoder.encode("Server error while adding book.", "UTF-8"));
        }
    }
}
