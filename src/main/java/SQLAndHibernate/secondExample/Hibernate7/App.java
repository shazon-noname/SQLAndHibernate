package SQLAndHibernate.secondExample.Hibernate7;

import SQLAndHibernate.secondExample.Hibernate7.util.HibernateUtil;
import SQLAndHibernate.secondExample.models.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;



public class App {
    public static void main(String[] args) {
        try (EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();

            transaction.begin();

            ShowAllDataBase showAllDataBase = new ShowAllDataBase();

            showAllDataBase.printAll(entityManager);

            transaction.commit();
        }
    }


}
