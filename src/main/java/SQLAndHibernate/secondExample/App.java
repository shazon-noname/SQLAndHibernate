package SQLAndHibernate.secondExample;

import SQLAndHibernate.secondExample.models.LinkedPurchaseList;
import SQLAndHibernate.secondExample.models.LinkedPurchaseListKey;
import SQLAndHibernate.secondExample.models.PurchaseList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class App {
    public static void main(String[] args) {
        StandardServiceRegistry standardServiceRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();
        Metadata metadata = new MetadataSources(standardServiceRegistry)
                .buildMetadata();
        SessionFactory sessionFactory = metadata.buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        List<PurchaseList> fromPurchaseList = session.createQuery("from PurchaseList", PurchaseList.class).list();
        for (PurchaseList purchase : fromPurchaseList) {
            Integer studentId = session.createQuery(
                            "select id from Student where name = :name",Integer.class)
                    .setParameter("name", purchase.getStudentName())
                    .uniqueResult();

            Integer courseId = session.createQuery(
                            "select id from Course where name = :name", Integer.class)
                    .setParameter("name", purchase.getCourseName())
                    .uniqueResult();

            LinkedPurchaseListKey key = new LinkedPurchaseListKey();
            key.setStudentId(studentId);
            key.setCourseId(courseId);

            LinkedPurchaseList linked = new LinkedPurchaseList();
            linked.setId(key);

            session.persist(linked);
        }

        tx.commit();
        session.close();
    }
}
