package in.kenz.dashboard.dao.impl;

import in.kenz.dashboard.dao.RoleDao;
import in.kenz.dashboard.entity.Role;
import in.kenz.dashboard.util.EntityManagerFactoryProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class RoleDaoImpl implements RoleDao {

    @Override
    public void save(Role role) {
        if (role == null) throw new IllegalArgumentException("Role cannot be null");

        try (EntityManager entityManager = EntityManagerFactoryProvider.getEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();

            try {
                transaction.begin();

                if (role.getRoleId() == null) {
                    entityManager.persist(role);
                    System.out.println("New Role saved");
                } else {
                    entityManager.merge(role);
                    System.out.println("Existing Role updated");
                }

                transaction.commit();
            } catch (RuntimeException e) {
                if (transaction.isActive()) transaction.rollback();
                throw new RuntimeException("Role cannot be saved", e);
            }
        }
    }

    @Override
    public Role findByRoleId(Long roleId) {
        if (roleId == null) throw new IllegalArgumentException("Role Id cannot be null");

        try (EntityManager entityManager = EntityManagerFactoryProvider.getEntityManager()) {
            return entityManager.find(Role.class, roleId);
        }
    }

    @Override
    public Role findByRoleName(String roleName) {
        if (roleName == null) throw new IllegalArgumentException("Role Name cannot be null");

        try (EntityManager entityManager = EntityManagerFactoryProvider.getEntityManager()) {

            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Role> criteriaQuery = criteriaBuilder.createQuery(Role.class);
            Root<Role> root = criteriaQuery.from(Role.class);

            Predicate predicate = criteriaBuilder.equal(root.get("roleName"), roleName);
            criteriaQuery.select(root).where(predicate);

            return entityManager.createQuery(criteriaQuery).getSingleResult();
        }
    }

    @Override
    public void deleteByRoleId(Long roleId) {
        if (roleId == null) throw new IllegalArgumentException("Role ID cannot be null");

        try (EntityManager entityManager = EntityManagerFactoryProvider.getEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();

            try {
                transaction.begin();

                Role role = entityManager.find(Role.class, roleId);
                if (role != null) {
                    entityManager.remove(role);
                    System.out.println("Role deleted");
                }

                transaction.commit();
            } catch (RuntimeException e) {
                if (transaction.isActive()) transaction.rollback();
                throw new RuntimeException("Role cannot be deleted", e);
            }
        }
    }
}
