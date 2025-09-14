package SQLAndHibernate.secondExample;

import SQLAndHibernate.secondExample.models.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Comparator;
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
//        secondExamplePurchaseList(session);
//        findCourse(session);
//        firstExample(session);
        findMultipleValues(session);

        tx.commit();
        session.close();
    }

    private static void firstExample(Session session) {
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
    }

    public static void secondExamplePurchaseList(Session session) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<PurchaseList> purchaseListCriteriaQuery = criteriaBuilder.createQuery(PurchaseList.class);
        Root<PurchaseList> purchaseListRoot = purchaseListCriteriaQuery.from(PurchaseList.class);
        purchaseListCriteriaQuery.select(purchaseListRoot);

        List<PurchaseList> resultList = session.createQuery(purchaseListCriteriaQuery).getResultList();
        for (PurchaseList purchase : resultList) {
            CriteriaQuery<Integer> studentCriteria = criteriaBuilder.createQuery(Integer.class);
            Root<Student> studentRoot = studentCriteria.from(Student.class);
            studentCriteria.select(studentRoot.get("id")).where(criteriaBuilder.equal(studentRoot.get("name"), purchase.getStudentName()));
            Integer studentId = session.createQuery(studentCriteria).uniqueResult();

            CriteriaQuery<Integer> courseCriteria = criteriaBuilder.createQuery(Integer.class);
            Root<Course> courseRoot = courseCriteria.from(Course.class);
            courseCriteria.select(courseRoot.get("id")).where(criteriaBuilder.equal(courseRoot.get("name"), purchase.getCourseName()));
            Integer courseId = session.createQuery(courseCriteria).uniqueResult();

            LinkedPurchaseListKey key = new LinkedPurchaseListKey();
            key.setStudentId(studentId);
            key.setCourseId(courseId);

            LinkedPurchaseList linked = new LinkedPurchaseList();
            linked.setId(key);

            session.persist(linked);
        }
    }

    public static void findCourse(Session session) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);
        Root<Course> root = criteriaQuery.from(Course.class);
        criteriaQuery.select(root)
                .where(criteriaBuilder.greaterThan(root.get("price"), 1000))
                .orderBy(criteriaBuilder.desc(root.get("price")));

        List<Course> resultList = session.createQuery(criteriaQuery).getResultList();

        resultList.forEach(course -> System.out.println(course.getName() + " - " + course.getPrice()));
    }

    public static void findMultipleValues(Session session) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Course> root = criteriaQuery.from(Course.class);

        Path<Object> namePath = root.get("name");
        Path<Object> pricePath = root.get("price");
        Path<Object> durationPath = root.get("duration");

        criteriaQuery.select(criteriaBuilder.array(namePath,pricePath,durationPath));

        List<Object[]> list = session.createQuery(criteriaQuery).list();
        System.out.printf("%40s | %10s | %10s%n", "Name", "Price", "Duration");
        System.out.println("---------------------------------------------------------------");

        list.sort(Comparator.comparing((Object[] row ) -> (Integer) row[1]).reversed());


        list.forEach(row -> {
            String name = (String) row[0];
            Integer price = (Integer) row[1];
            Integer duration = (Integer) row[2];

            System.out.printf("%-40s | %-10d | %-10d%n", name, price, duration);
        });
    }
}
