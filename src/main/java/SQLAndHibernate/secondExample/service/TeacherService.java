package SQLAndHibernate.secondExample.service;

import SQLAndHibernate.secondExample.models.Teacher;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.Comparator;
import java.util.List;

public class TeacherService {

    public static void findAllOrderedBySalary(Session session) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = criteriaBuilder.createQuery(Tuple.class);
        Root<Teacher> root = query.from(Teacher.class);

        query.select(criteriaBuilder.tuple(
                root.get("name"),
                root.get("age"),
                root.get("salary")
        ));

        List<Tuple> list = session.createQuery(query).getResultList();
        list.sort(Comparator.comparing((Tuple row) -> row.get(2, Integer.class)).reversed());

        System.out.printf("|%-40s|%-10s|%-10s|%n", "TEACHER", "AGE", "SALARY");
        list.forEach(row -> {
            String name = row.get(0, String.class);
            Integer age = row.get(1, Integer.class);
            Integer salary = row.get(2, Integer.class);

            System.out.println("-".repeat(64));
            System.out.printf("|%-40s|%-10s|%-10s|%n", name, age, salary);
        });
    }
}
