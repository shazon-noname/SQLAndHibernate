package SQLAndHibernate.secondExample.service;

import SQLAndHibernate.secondExample.models.Course;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.Comparator;
import java.util.List;

public class CourseService {
    
    public static void findExpensiveCourses(Session session) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);
        Root<Course> root = criteriaQuery.from(Course.class);
        criteriaQuery.select(root)
                .where(criteriaBuilder.greaterThan(root.get("price"), 1000))
                .orderBy(criteriaBuilder.desc(root.get("price")));

        List<Course> resultList = session.createQuery(criteriaQuery).getResultList();
        resultList.forEach(course -> System.out.println(course.getName() + " - " + course.getPrice()));
    }

    public static void findCoursesWithDetails(Session session) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Course> root = criteriaQuery.from(Course.class);

        criteriaQuery.multiselect(
            root.get("name"),
            root.get("price"),
            root.get("duration")
        );

        List<Object[]> list = session.createQuery(criteriaQuery).list();
        System.out.printf("%40s | %10s | %10s%n", "Name", "Price", "Duration");
        System.out.println("-".repeat(64));

        list.sort(Comparator.comparing((Object[] row) -> (Integer) row[1]).reversed());

        for (Object[] row : list) {
            String name = (String) row[0];
            Integer price = (Integer) row[1];
            Integer duration = (Integer) row[2];
            System.out.printf("%-40s | %-10d | %-10d%n", name, price, duration);
        }
    }
}
