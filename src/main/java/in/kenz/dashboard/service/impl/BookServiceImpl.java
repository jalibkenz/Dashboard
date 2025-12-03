package in.kenz.dashboard.service.impl;

import in.kenz.dashboard.dao.BookDao;
import in.kenz.dashboard.dao.LoanDao;
import in.kenz.dashboard.dao.impl.BookDaoImpl;
import in.kenz.dashboard.dao.impl.LoanDaoImpl;
import in.kenz.dashboard.entity.Book;
import in.kenz.dashboard.entity.Loan;
import in.kenz.dashboard.service.BookService;
import in.kenz.dashboard.util.EntityManagerFactoryProvider;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    //  DASHBOARD METHODS (upgraded)
    // ------------------------------

    /**
     * Return list of Books currently loaned out.
     * Criteria steps:
     * 1. get CriteriaBuilder
     * 2. create CriteriaQuery<Book>
     * 3. root = Loan
     * 4. join Loan -> Book
     * 5. predicate: returnedDate IS NULL
     * 6. select bookJoin and execute
     */
    @Override
    public List<Book> findLoanedBooks() {
        EntityManager em = EntityManagerFactoryProvider.getEntityManager();
        try {
            // 1. CriteriaBuilder
            CriteriaBuilder cb = em.getCriteriaBuilder();

            // 2. CriteriaQuery<Book>
            CriteriaQuery<Book> cq = cb.createQuery(Book.class);

            // 3. Root = Loan
            Root<Loan> loanRoot = cq.from(Loan.class);

            // 4. Join Loan -> Book
            // use inner join (loan must have a book)
            Join<Loan, Book> bookJoin = loanRoot.join("loanAvailedBook", JoinType.INNER);

            // 5. Predicate: returnedDate IS NULL (active loans)
            Predicate activeLoan = cb.isNull(loanRoot.get("returnedDate"));

            // 6. SELECT bookJoin WHERE activeLoan
            cq.select(bookJoin).where(activeLoan).distinct(true);

            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Return list of Books currently available (no active Loan).
     * Implementation uses a NOT EXISTS subquery:
     * WHERE NOT EXISTS (
     *   SELECT 1 FROM Loan l WHERE l.loanAvailedBook = book AND l.returnedDate IS NULL
     * )
     */
    @Override
    public List<Book> findAvailableBooks() {
        EntityManager em = EntityManagerFactoryProvider.getEntityManager();
        try {
            // 1. CriteriaBuilder
            CriteriaBuilder cb = em.getCriteriaBuilder();

            // 2. CriteriaQuery<Book>
            CriteriaQuery<Book> cq = cb.createQuery(Book.class);

            // 3. Root = Book
            Root<Book> bookRoot = cq.from(Book.class);

            // 4. Subquery setup
            Subquery<Long> sub = cq.subquery(Long.class);
            Root<Loan> loanRoot = sub.from(Loan.class);

            // match loan.loanAvailedBook == bookRoot AND returnedDate IS NULL
            Predicate sameBook = cb.equal(loanRoot.get("loanAvailedBook"), bookRoot);
            Predicate notReturned = cb.isNull(loanRoot.get("returnedDate"));
            sub.select(cb.literal(1L)).where(cb.and(sameBook, notReturned));

            // 5. WHERE NOT EXISTS(sub)
            cq.select(bookRoot).where(cb.not(cb.exists(sub)));

            // 6. execute
            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }
}
