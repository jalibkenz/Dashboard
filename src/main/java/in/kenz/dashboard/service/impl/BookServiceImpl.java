package in.kenz.dashboard.service.impl;

import in.kenz.dashboard.dao.BookDao;
import in.kenz.dashboard.dao.LoanDao;
import in.kenz.dashboard.dao.impl.BookDaoImpl;
import in.kenz.dashboard.dao.impl.LoanDaoImpl;
import in.kenz.dashboard.entity.Book;
import in.kenz.dashboard.entity.Loan;
import in.kenz.dashboard.service.BookService;

import java.util.ArrayList;
import java.util.List;

public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final LoanDao loanDao;

    public BookServiceImpl() {
        this.bookDao = new BookDaoImpl();
        this.loanDao = new LoanDaoImpl();
    }

    // ------------------------------
    //  BASIC CRUD
    // ------------------------------

    @Override
    public void save(Book book) {
        if (book == null) throw new IllegalArgumentException("Book cannot be null");

        if (book.getBookName() == null || book.getBookName().trim().isEmpty())
            throw new IllegalArgumentException("Book name cannot be empty");

        if (book.getBookAuthor() == null || book.getBookAuthor().trim().isEmpty())
            throw new IllegalArgumentException("Book author cannot be empty");

        // Optional, but clean
        if (book.getBookCategory() == null)
            book.setBookCategory("");

        bookDao.save(book);
    }

    @Override
    public Book findById(Long id) {
        if (id == null) throw new IllegalArgumentException("Book ID cannot be null");
        return bookDao.findByBookId(id);
    }

    @Override
    public List<Book> findAll() {
        return bookDao.findAll();
    }

    @Override
    public void delete(Long id) {
        if (id == null) throw new IllegalArgumentException("Book ID cannot be null");
        bookDao.deleteByBookId(id);
    }



    // ------------------------------
    //  DASHBOARD METHODS
    // ------------------------------

    @Override
    public List<Book> findLoanedBooks() {
        List<Loan> activeLoans = loanDao.findActiveLoans();
        List<Book> result = new ArrayList<>();

        for (Loan loan : activeLoans) {
            if (loan.getLoanAvailedBook() != null) {
                result.add(loan.getLoanAvailedBook());
            }
        }

        return result;
    }

    @Override
    public List<Book> findAvailableBooks() {
        List<Book> allBooks = bookDao.findAll();
        List<Loan> activeLoans = loanDao.findActiveLoans();

        // Collect IDs of loaned books
        List<Long> loanedBookIds = new ArrayList<>();
        for (Loan loan : activeLoans) {
            if (loan.getLoanAvailedBook() != null) {
                loanedBookIds.add(loan.getLoanAvailedBook().getBookId());
            }
        }

        // Filter available books
        List<Book> available = new ArrayList<>();
        for (Book book : allBooks) {
            if (!loanedBookIds.contains(book.getBookId())) {
                available.add(book);
            }
        }

        return available;
    }
}
