package SQLAndHibernate.secondExample.Hibernate7.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class HibernateUtil {
    private static final EntityManagerFactory emf;

    static {
        try {
            emf = Persistence.createEntityManagerFactory("my-persistence-unit");
        } catch (Throwable ex) {
            System.out.println("EntityManagerFactory init failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public static void shutDown() {
        if (emf != null) {
            emf.close();
        }
    }
}
