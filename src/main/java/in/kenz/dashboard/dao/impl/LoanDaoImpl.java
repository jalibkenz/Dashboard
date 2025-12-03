package in.kenz.dashboard.dao.impl;

import in.kenz.dashboard.dao.LoanDao;
import in.kenz.dashboard.entity.Book;
import in.kenz.dashboard.entity.Loan;
import in.kenz.dashboard.entity.User;
import in.kenz.dashboard.util.EntityManagerFactoryProvider;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class LoanDaoImpl implements LoanDao {

    @Override
    public void save(Loan loan) {
        // DAO shouldn't validate; just persist or merge inside a transaction
        EntityManager em = EntityManagerFactoryProvider.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (loan.getLoanId() == null) {
                em.persist(loan);
                System.out.println("New Loan saved");
            } else {
                em.merge(loan);
                System.out.println("Existing Loan merged");
            }
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Loan loan) {
        // separate update method (merge)
        EntityManager em = EntityManagerFactoryProvider.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(loan);
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    @Override
    public Loan findByLoanId(Long loanId) {
        if (loanId == null) return null;
        EntityManager em = EntityManagerFactoryProvider.getEntityManager();
        try {
            return em.find(Loan.class, loanId);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Loan> findAll() {
        try (EntityManager em = EntityManagerFactoryProvider.getEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Loan> cq = cb.createQuery(Loan.class);
            Root<Loan> root = cq.from(Loan.class);
            cq.select(root).orderBy(cb.desc(root.get("loanAvailingDate")));
            TypedQuery<Loan> q = em.createQuery(cq);
            return q.getResultList();
        }
    }

    @Override
    public List<Loan> findActiveLoans() {
        try (EntityManager em = EntityManagerFactoryProvider.getEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Loan> cq = cb.createQuery(Loan.class);
            Root<Loan> root = cq.from(Loan.class);

            // returnedDate IS NULL => active
            Predicate activePred = cb.isNull(root.get("returnedDate"));
            cq.select(root).where(activePred).orderBy(cb.desc(root.get("loanAvailingDate")));

            TypedQuery<Loan> q = em.createQuery(cq);
            return q.getResultList();
        }
    }

    @Override
    public List<Loan> findActiveLoansByBook(Book book) {
        if (book == null) return Collections.emptyList();
        try (EntityManager em = EntityManagerFactoryProvider.getEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Loan> cq = cb.createQuery(Loan.class);
            Root<Loan> root = cq.from(Loan.class);

            Predicate byBook = cb.equal(root.get("loanAvailedBook"), book);
            Predicate active = cb.isNull(root.get("returnedDate"));
            cq.select(root).where(cb.and(byBook, active));

            TypedQuery<Loan> q = em.createQuery(cq);
            return q.getResultList();
        }
    }

    @Override
    public List<Loan> findActiveLoansByUser(User user) {
        if (user == null) return Collections.emptyList();
        try (EntityManager em = EntityManagerFactoryProvider.getEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Loan> cq = cb.createQuery(Loan.class);
            Root<Loan> root = cq.from(Loan.class);

            Predicate byUser = cb.equal(root.get("loanAvailingUser"), user);
            Predicate active = cb.isNull(root.get("returnedDate"));
            cq.select(root).where(cb.and(byUser, active)).orderBy(cb.desc(root.get("loanAvailingDate")));

            TypedQuery<Loan> q = em.createQuery(cq);
            return q.getResultList();
        }
    }

    @Override
    public List<Loan> findByLoanAvailingUser(User user) {
        if (user == null) return Collections.emptyList();
        try (EntityManager em = EntityManagerFactoryProvider.getEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Loan> cq = cb.createQuery(Loan.class);
            Root<Loan> root = cq.from(Loan.class);

            Predicate byUser = cb.equal(root.get("loanAvailingUser"), user);
            cq.select(root).where(byUser).orderBy(cb.desc(root.get("loanAvailingDate")));

            TypedQuery<Loan> q = em.createQuery(cq);
            return q.getResultList();
        }
    }

    @Override
    public List<Loan> findByLoanAvailedBook(Book book) {
        if (book == null) return Collections.emptyList();
        try (EntityManager em = EntityManagerFactoryProvider.getEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Loan> cq = cb.createQuery(Loan.class);
            Root<Loan> root = cq.from(Loan.class);

            Predicate byBook = cb.equal(root.get("loanAvailedBook"), book);
            cq.select(root).where(byBook).orderBy(cb.desc(root.get("loanAvailingDate")));

            TypedQuery<Loan> q = em.createQuery(cq);
            return q.getResultList();
        }
    }

    @Override
    public List<Loan> findByLoanAvailingDate(LocalDate loanDate) {
        if (loanDate == null) return Collections.emptyList();
        try (EntityManager em = EntityManagerFactoryProvider.getEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Loan> cq = cb.createQuery(Loan.class);
            Root<Loan> root = cq.from(Loan.class);

            Predicate byDate = cb.equal(root.get("loanAvailingDate"), loanDate);
            cq.select(root).where(byDate).orderBy(cb.desc(root.get("loanAvailingDate")));

            TypedQuery<Loan> q = em.createQuery(cq);
            return q.getResultList();
        }
    }

    @Override
    public void deleteByLoanId(Long loanId) {
        if (loanId == null) return;
        EntityManager em = EntityManagerFactoryProvider.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Loan loan = em.find(Loan.class, loanId);
            if (loan != null) em.remove(loan);
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
}
