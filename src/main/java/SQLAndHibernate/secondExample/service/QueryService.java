package SQLAndHibernate.secondExample.service;

import SQLAndHibernate.secondExample.models.*;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;

import java.util.List;

public class QueryService {

    public static void fetchDetailedStudentInfo(Session session) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = criteriaBuilder.createQuery(Tuple.class);

        Root<Student> studentRoot = query.from(Student.class);
        Root<Course> courseRoot = query.from(Course.class);
        Root<Teacher> teacherRoot = query.from(Teacher.class);
        Root<Subscription> subscriptionRoot = query.from(Subscription.class);

        query.select(criteriaBuilder.tuple(
                studentRoot,
                courseRoot,
                teacherRoot,
                subscriptionRoot
        ));

        // Build predicates for each entity
        Predicate studentRestriction = criteriaBuilder.and(
                criteriaBuilder.greaterThan(studentRoot.get("age"), 18),
                criteriaBuilder.isNotNull(studentRoot.get("registrationDate"))
        );

        Predicate courseRestriction = criteriaBuilder.and(
                criteriaBuilder.like(courseRoot.get("name"), "%Java%"),
                criteriaBuilder.greaterThan(courseRoot.get("price"), 1000)
        );

        Predicate teacherRestriction = criteriaBuilder.and(
                criteriaBuilder.equal(teacherRoot.get("salary"), 5000)
        );

        Predicate subscriptionRestriction = criteriaBuilder.and(
                criteriaBuilder.equal(subscriptionRoot.get("studentId"), studentRoot.get("id")),
                criteriaBuilder.equal(subscriptionRoot.get("courseId"), courseRoot.get("id")),
                criteriaBuilder.equal(courseRoot.get("teacher"), teacherRoot)
        );

        query.where(criteriaBuilder.and(
                studentRestriction,
                courseRestriction,
                teacherRestriction,
                subscriptionRestriction
        ));

        List<Tuple> resultList = session.createQuery(query).getResultList();

        // Print results
        System.out.printf("%-20s | %-30s | %-20s | %s%n",
                "STUDENT", "COURSE", "TEACHER", "SUBSCRIPTION DATE");
        System.out.println("-".repeat(100));

        for (Tuple tuple : resultList) {
            Student student = tuple.get(0, Student.class);
            Course course = tuple.get(1, Course.class);
            Teacher teacher = tuple.get(2, Teacher.class);
            Subscription subscription = tuple.get(3, Subscription.class);

            System.out.printf("%-20s | %-30s | %-20s | %s%n",
                    student.getName(),
                    course.getName(),
                    teacher.getName(),
                    subscription.getSubscriptionDate());
        }
    }
}
