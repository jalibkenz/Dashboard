package in.kenz.dashboard.dao.impl;

import in.kenz.dashboard.dao.UserDao;
import in.kenz.dashboard.entity.User;
import in.kenz.dashboard.util.EntityManagerFactoryProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.*;

import java.util.List;

public class UserDaoImpl implements UserDao {

    @Override
    public void save(User user) {
        try (EntityManager entityManager = EntityManagerFactoryProvider.getEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                if (user.getUserId() == null) {
                    entityManager.persist(user); // new user -> save
                    System.out.println("->New User Saved");
                } else {
                    entityManager.merge(user); // existing user -> update
                    System.out.println("->Existing User Updated");
                }
                transaction.commit();
            } catch (RuntimeException e) {
                if (transaction.isActive()) transaction.rollback();
                throw new RuntimeException("User cannot be saved", e);
            }
        }
    }

    @Override
    public User findByUserId(Long userId) {
        try (EntityManager entityManager = EntityManagerFactoryProvider.getEntityManager()) {
            return entityManager.find(User.class, userId);
        }
    }

    @Override
    public User findByUserName(String userName) {
        if (userName == null) throw new IllegalArgumentException("User Name cannot be null");

        try (EntityManager entityManager = EntityManagerFactoryProvider.getEntityManager()) {

            //Criteria API
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);

            Predicate predicate = criteriaBuilder.equal(root.get("userName"), userName);

            criteriaQuery.select(root).where(predicate);
            return entityManager.createQuery(criteriaQuery).getSingleResult();


        }
    }

    @Override
    public User findByUserUsername(String userUsername) {
        if (userUsername == null) throw new IllegalArgumentException("Username cannot be null");
        try (EntityManager entityManager = EntityManagerFactoryProvider.getEntityManager()) {
            //Criteria API
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);

            Predicate predicate = criteriaBuilder.equal(root.get("userUsername"), userUsername);
            criteriaQuery.select(root).where(predicate);


            List<User> userList = entityManager.createQuery(criteriaQuery).getResultList();
            return userList.isEmpty() ? null : userList.get(0);

        }


    }



    @Override
    public User findByUserRoleName(String userRoleName) {
        if(userRoleName==null) throw new IllegalArgumentException("User RoleName cannot be null");

        try(EntityManager entityManager = EntityManagerFactoryProvider.getEntityManager()){

            //Criteria API
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);

            Predicate predicate = criteriaBuilder.equal(root.get("userRoleName"), userRoleName);
            criteriaQuery.select(root).where(predicate);
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        }
    }

    @Override
    public void deleteByUserId(Long userId) {
        if(userId==null) throw new IllegalArgumentException("User ID cannot be empty to delete user");

        try(EntityManager entityManager = EntityManagerFactoryProvider.getEntityManager()){
            EntityTransaction transaction = entityManager.getTransaction();
            try{
                transaction.begin();
                User user = entityManager.find(User.class, userId);
                entityManager.remove(user);
                transaction.commit();
                System.out.println("User deleted successfully");
            } catch (RuntimeException e) {
                if(transaction.isActive()) transaction.rollback();
                throw new RuntimeException("User cannot be deleted",e);
            }
        }

    }

}
