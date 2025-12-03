package in.kenz.dashboard.service;

import in.kenz.dashboard.entity.Book;
import in.kenz.dashboard.entity.Loan;
import in.kenz.dashboard.entity.User;

import java.util.Date;
import java.util.List;

public interface LoanService {

    // Create a new loan
    Loan createLoan(User user, Long bookId, Date dueDate);

    // Books currently loaned by a specific user
    List<Book> findLoanedBooksByUser(User user);

    // All active loans (returnedDate IS NULL)
    List<Loan> findActiveLoans();
}
