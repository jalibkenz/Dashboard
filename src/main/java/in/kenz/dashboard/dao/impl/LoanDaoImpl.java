package in.kenz.dashboard.dao.impl;

import in.kenz.dashboard.dao.LoanDao;
import in.kenz.dashboard.entity.Book;
import in.kenz.dashboard.entity.Loan;
import in.kenz.dashboard.entity.User;
import in.kenz.dashboard.util.EntityManagerFactoryProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;

public class LoanDaoImpl implements LoanDao {

    @Override
    public void save(Loan loan) {
        if (loan == null) throw new IllegalArgumentException("Loan cannot be null");

        try (EntityManager entityManager = EntityManagerFactoryProvider.getEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();

                if (loan.getLoanId() == null) {
                    entityManager.persist(loan);
                    System.out.println("New Loan saved");
                } else {
                    entityManager.merge(loan);
                    System.out.println("Existing Loan updated");
                }

                transaction.commit();
            } catch (RuntimeException e) {
                if (transaction.isActive()) transaction.rollback();
                throw new RuntimeException("Loan cannot be saved", e);
            }
        }
    }

    @Override
    public Loan findByLoanId(Long loanId) {
        if (loanId == null) throw new IllegalArgumentException("Loan Id cannot be null");

        try (EntityManager entityManager = EntityManagerFactoryProvider.getEntityManager()) {
            return entityManager.find(Loan.class, loanId);
        }
    }

    @Override
    public Loan findByLoanAvailingUser(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");

        try (EntityManager entityManager = EntityManagerFactoryProvider.getEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Loan> cq = cb.createQuery(Loan.class);
            Root<Loan> root = cq.from(Loan.class);

            Predicate predicate = cb.equal(root.get("loanAvailingUser"), user);
            cq.select(root).where(predicate);

            return entityManager.createQuery(cq).getSingleResult();
        }
    }

    @Override
    public Loan findByLoanAvailedBook(Book book) {
        if (book == null) throw new IllegalArgumentException("Book cannot be null");

        try (EntityManager entityManager = EntityManagerFactoryProvider.getEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Loan> cq = cb.createQuery(Loan.class);
            Root<Loan> root = cq.from(Loan.class);

            Predicate predicate = cb.equal(root.get("loanAvailedBook"), book);
            cq.select(root).where(predicate);

            return entityManager.createQuery(cq).getSingleResult();
        }
    }

    @Override
    public Loan findByLoanAvailingDate(LocalDate localDate) {
        if (localDate == null) throw new IllegalArgumentException("Loan Availing Date cannot be null");

        try (EntityManager entityManager = EntityManagerFactoryProvider.getEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Loan> cq = cb.createQuery(Loan.class);
            Root<Loan> root = cq.from(Loan.class);

            Predicate predicate = cb.equal(root.get("loanAvailingDate"), localDate);
            cq.select(root).where(predicate);

            return entityManager.createQuery(cq).getSingleResult();
        }
    }

    @Override
    public void deleteByLoanId(Long loanId) {
        if (loanId == null) throw new IllegalArgumentException("Loan ID cannot be null");

        try (EntityManager entityManager = EntityManagerFactoryProvider.getEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();

            try {
                transaction.begin();
                Loan loan = entityManager.find(Loan.class, loanId);
                entityManager.remove(loan);
                transaction.commit();
                System.out.println("Loan deleted");
            } catch (RuntimeException e) {
                if (transaction.isActive()) transaction.rollback();
                throw new RuntimeException("Loan cannot be deleted", e);
            }
        }
    }
}
