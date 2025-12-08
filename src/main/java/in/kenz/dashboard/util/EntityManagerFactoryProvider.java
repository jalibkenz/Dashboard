package in.kenz.dashboard.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

//public class JPAUtil {
//
//    private static final EntityManagerFactory entityManagerFactory;
//
//    static {
//        try {
//            entityManagerFactory = Persistence.createEntityManagerFactory("persistenceUnit");
//        } catch (Exception e) {
//            System.err.println("EntityManagerFactory initialization failed.");
//            throw new ExceptionInInitializerError(e);
//        }
//    }
//
//    public static EntityManager getEntityManager() {
//        return entityManagerFactory.createEntityManager();
//    }
//
//    public static void close() {
//        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
//            entityManagerFactory.close();
//        }
//    }
//}




public class EntityManagerFactoryProvider {

    private static final EntityManagerFactory entityManagerFactory;

    static {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("persistenceUnit");
        } catch (Exception e) {
            System.err.println("EntityManagerFactory initialization failed.");
            throw new ExceptionInInitializerError(e);
        }
    }

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public static void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
