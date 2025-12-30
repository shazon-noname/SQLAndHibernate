package SQLAndHibernate.secondExample.service;

import SQLAndHibernate.secondExample.models.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.List;

public class PurchaseService {

    public static void processPurchaseList(Session session) {
        List<PurchaseList> purchases = session.createQuery("from PurchaseList", PurchaseList.class).list();
        
        for (PurchaseList purchase : purchases) {
            Integer studentId = session.createQuery(
                            "select id from Student where name = :name", Integer.class)
                    .setParameter("name", purchase.getStudentName())
                    .uniqueResult();

            Integer courseId = session.createQuery(
                            "select id from Course where name = :name", Integer.class)
                    .setParameter("name", purchase.getCourseName())
                    .uniqueResult();

            if (studentId != null && courseId != null) {
                LinkedPurchaseListKey key = new LinkedPurchaseListKey();
                key.setStudentId(studentId);
                key.setCourseId(courseId);

                LinkedPurchaseList linked = new LinkedPurchaseList();
                linked.setId(key);

                session.persist(linked);
            }
        }
    }

    public static void processPurchaseListWithCriteria(Session session) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<PurchaseList> purchaseListQuery = criteriaBuilder.createQuery(PurchaseList.class);
        Root<PurchaseList> purchaseRoot = purchaseListQuery.from(PurchaseList.class);
        purchaseListQuery.select(purchaseRoot);

        List<PurchaseList> purchases = session.createQuery(purchaseListQuery).getResultList();
        
        for (PurchaseList purchase : purchases) {
            // Find student ID
            CriteriaQuery<Integer> studentQuery = criteriaBuilder.createQuery(Integer.class);
            Root<Student> studentRoot = studentQuery.from(Student.class);
            studentQuery.select(studentRoot.get("id"))
                    .where(criteriaBuilder.equal(studentRoot.get("name"), purchase.getStudentName()));
            Integer studentId = session.createQuery(studentQuery).uniqueResult();

            // Find course ID
            CriteriaQuery<Integer> courseQuery = criteriaBuilder.createQuery(Integer.class);
            Root<Course> courseRoot = courseQuery.from(Course.class);
            courseQuery.select(courseRoot.get("id"))
                    .where(criteriaBuilder.equal(courseRoot.get("name"), purchase.getCourseName()));
            Integer courseId = session.createQuery(courseQuery).uniqueResult();

            if (studentId != null && courseId != null) {
                LinkedPurchaseListKey key = new LinkedPurchaseListKey();
                key.setStudentId(studentId);
                key.setCourseId(courseId);

                LinkedPurchaseList linked = new LinkedPurchaseList();
                linked.setId(key);

                session.persist(linked);
            }
        }
    }
}
