package in.kenz.dashboard.dao.impl;

import in.kenz.dashboard.dao.BookDao;
import in.kenz.dashboard.entity.Book;
import in.kenz.dashboard.entity.User;
import in.kenz.dashboard.util.EntityManagerFactoryProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class BookDaoImpl implements BookDao {
    @Override
    public void save(Book book) {
        try(EntityManager entityManager = EntityManagerFactoryProvider.getEntityManager()){
            EntityTransaction transaction = entityManager.getTransaction();
            try{
                transaction.begin();
                if (book.getBookId()==null) {
                    entityManager.persist(book);
                    System.out.println("New Book saved");
                }
                else {
                    entityManager.merge(book);
                    System.out.println("Existing Book updated");
                }
                transaction.commit();
            } catch (RuntimeException e) {
                if (transaction.isActive()) transaction.rollback();
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Book findByBookId(Long bookId) {
        if(bookId==null) throw new IllegalArgumentException("Book Id cannot be null");
        try(EntityManager entityManager = EntityManagerFactoryProvider.getEntityManager()){
            return entityManager.find(Book.class,bookId);
        }
    }

    @Override
    public Book findByBookName(String bookName) {
        try(EntityManager entityManager = EntityManagerFactoryProvider.getEntityManager()){
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
            Root<Book> root = criteriaQuery.from(Book.class);

            Predicate predicate = criteriaBuilder.equal(root.get("bookName"), bookName);
            criteriaQuery.select(root).where(predicate);

            return entityManager.createQuery(criteriaQuery).getSingleResult();

        }

    }

    @Override
    public Book findByBookCategory(String bookCategory) {

        try(EntityManager entityManager = EntityManagerFactoryProvider.getEntityManager()){
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);

            Root<Book> root = criteriaQuery.from(Book.class);

            Predicate predicate = criteriaBuilder.equal(root.get("bookCategory"), bookCategory);
            criteriaQuery.select(root).where(predicate);
            return entityManager.createQuery(criteriaQuery).getSingleResult();

        }
    }

    @Override
    public Book findByBookAuthor(String bookAuthor) {
        try(EntityManager entityManager = EntityManagerFactoryProvider.getEntityManager()){
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
            Root<Book> root = criteriaQuery.from(Book.class);

            Predicate predicate = criteriaBuilder.equal(root.get("bookAuthor"), bookAuthor);
            criteriaQuery.select(root).where(predicate);
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        }
    }

    @Override
    public List<Book> findAll() {
        try (EntityManager entityManager = EntityManagerFactoryProvider.getEntityManager()) {

            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
            Root<Book> root = criteriaQuery.from(Book.class);

            Predicate predicate = criteriaBuilder.conjunction();

            criteriaQuery.select(root).where(predicate).orderBy(criteriaBuilder.asc(root.get("bookName")));
            return entityManager.createQuery(criteriaQuery).getResultList();
        }
    }


    @Override
    public void deleteByBookId(Long bookId) {
        try (EntityManager entityManager = EntityManagerFactoryProvider.getEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                Book book = entityManager.find(Book.class, bookId);
                entityManager.remove(book);
                transaction.commit();
                System.out.println("Book deleted");
            } catch (RuntimeException e) {
                if (transaction.isActive()) transaction.rollback();
                throw new RuntimeException("Book cannot be deleted", e);
            }
        }
    }
}
