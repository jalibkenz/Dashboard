package in.kenz.dashboard.service.impl;

import in.kenz.dashboard.entity.Book;
import in.kenz.dashboard.entity.Loan;
import in.kenz.dashboard.entity.User;
import in.kenz.dashboard.service.LoanService;
import in.kenz.dashboard.util.EntityManagerFactoryProvider;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class LoanServiceImpl implements LoanService {

    private static final int DEFAULT_LOAN_DAYS = 30;

    @Override
    public Loan createLoan(User user, Long bookId, Date dueDate) {
        EntityManager em = EntityManagerFactoryProvider.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // 1) Lock the book row to prevent concurrent loans
            Book book = em.find(Book.class, bookId, LockModeType.PESSIMISTIC_WRITE);
            if (book == null) {
                throw new IllegalStateException("Book not found");
            }

            // ------------------------------------------------------------
            // ACTIVE LOAN CHECK using Criteria API (count)
            // ------------------------------------------------------------
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Loan> root = cq.from(Loan.class);

            // book equality check (use entity equality here because book is managed in this EM)
            Predicate sameBook = cb.equal(root.get("loanAvailedBook"), book);
            Predicate notReturned = cb.isNull(root.get("returnedDate"));

            cq.select(cb.count(root)).where(cb.and(sameBook, notReturned));

            Long activeCount = em.createQuery(cq).getSingleResult();
            if (activeCount != null && activeCount > 0) {
                throw new IllegalStateException("Book is already loaned");
            }
            // ------------------------------------------------------------

            // Convert incoming Date to LocalDate (or default to 30 days)
            LocalDate dueLocal;
            if (dueDate != null) {
                Instant inst = dueDate.toInstant();
                dueLocal = inst.atZone(ZoneId.systemDefault()).toLocalDate();
            } else {
                dueLocal = LocalDate.now().plusDays(DEFAULT_LOAN_DAYS);
            }

            // Create Loan using your constructor (status = ACTIVE)
            Loan loan = new Loan(user, book, LocalDate.now(), dueLocal);

            em.persist(loan);

            tx.commit();

            return loan;

        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Return list of Books currently loaned by the given user.
     * IMPORTANT: compare by userId (not entity) — works for detached user instances.
     */
    @Override
    public List<Book> findLoanedBooksByUser(User user) {
        if (user == null || user.getUserId() == null) {
            return Collections.emptyList();
        }

        EntityManager em = EntityManagerFactoryProvider.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Book> cq = cb.createQuery(Book.class);

            Root<Loan> loanRoot = cq.from(Loan.class);
            Join<Loan, Book> bookJoin = loanRoot.join("loanAvailedBook", JoinType.INNER);

            // compare by userId path, not by passing detached user entity
            Path<Long> userIdPath = loanRoot.get("loanAvailingUser").get("userId");
            Predicate byUserId = cb.equal(userIdPath, user.getUserId());
            Predicate active = cb.isNull(loanRoot.get("returnedDate"));

            cq.select(bookJoin).where(cb.and(byUserId, active)).distinct(true);

            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Return all active Loan entities (returnedDate IS NULL).
     * Useful for admin views or other service logic.
     */
    @Override
    public List<Loan> findActiveLoans() {
        EntityManager em = EntityManagerFactoryProvider.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Loan> cq = cb.createQuery(Loan.class);
            Root<Loan> root = cq.from(Loan.class);
            cq.select(root).where(cb.isNull(root.get("returnedDate")));
            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }
}
