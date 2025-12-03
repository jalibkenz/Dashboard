package in.kenz.dashboard.dao;

import in.kenz.dashboard.entity.Book;
import in.kenz.dashboard.entity.Loan;
import in.kenz.dashboard.entity.User;

import java.time.LocalDate;
import java.util.List;

/**
 * DAO layer handles ONLY database persistence and retrieval.
 * No business validation or rules here.
 */
public interface LoanDao {

    // --------------------------
    // CRUD
    // --------------------------

    /**
     * Persist a new loan.
     */
    void save(Loan loan);

    /**
     * Merge/update an existing loan.
     */
    void update(Loan loan);

    /**
     * Find loan by primary id.
     */
    Loan findByLoanId(Long loanId);

    /**
     * Delete loan by id.
     */
    void deleteByLoanId(Long loanId);


    // --------------------------
    // READ METHODS
    // --------------------------

    /**
     * Return all loans in database (full history).
     */
    List<Loan> findAll();

    /**
     * Return all ACTIVE loans where returnedDate IS NULL.
     */
    List<Loan> findActiveLoans();

    /**
     * Active loans of a user.
     */
    List<Loan> findActiveLoansByUser(User user);

    /**
     * Active loans for a specific book (usually 0 or 1).
     */
    List<Loan> findActiveLoansByBook(Book book);

    /**
     * Full loan history of a user.
     */
    List<Loan> findByLoanAvailingUser(User user);

    /**
     * Full loan history of a book.
     */
    List<Loan> findByLoanAvailedBook(Book book);

    /**
     * Loans taken on a given date.
     */
    List<Loan> findByLoanAvailingDate(LocalDate loanDate);
}
