package SQLAndHibernate.secondExample;

import SQLAndHibernate.secondExample.config.HibernateUtil;
import SQLAndHibernate.secondExample.service.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class App {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
             Session session = sessionFactory.openSession()) {
            
            Transaction tx = session.beginTransaction();
            
            // Example 1: Process purchase lists
            // PurchaseService.processPurchaseList(session);
            // PurchaseService.processPurchaseListWithCriteria(session);
            
            // Example 2: Find and display courses
            // CourseService.findExpensiveCourses(session);
            // CourseService.findCoursesWithDetails(session);
            
            // Example 3: Find and display teachers
            // TeacherService.findAllOrderedBySalary(session);
            
            // Example 4: Find and display students
            // StudentService.findAllWithDetails(session);
            
            // Example 5: Run complex query
            QueryService.fetchDetailedStudentInfo(session);
            
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}

