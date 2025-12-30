package SQLAndHibernate.secondExample.service;

import SQLAndHibernate.secondExample.models.DTO.StudentDTO;
import SQLAndHibernate.secondExample.models.Student;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.List;

public class StudentService {

    public static void findAllWithDetails(Session session) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<StudentDTO> criteriaQuery = criteriaBuilder.createQuery(StudentDTO.class);
        Root<Student> root = criteriaQuery.from(Student.class);

        criteriaQuery.select(criteriaBuilder.construct(StudentDTO.class,
                root.get("name"),
                root.get("age"),
                root.get("registrationDate")
        ));

        List<StudentDTO> resultList = session.createQuery(criteriaQuery).getResultList();
        
        // Calculate column widths
        int nameLength = resultList.stream()
                .mapToInt(dto -> dto.getName().length())
                .max()
                .orElse(10) + 2;
                
        int ageLength = resultList.stream()
                .mapToInt(dto -> String.valueOf(dto.getAge()).length())
                .max()
                .orElse(3) + 2;
                
        int regDateLength = resultList.stream()
                .mapToInt(dto -> dto.getRegistrationDate().toString().length())
                .max()
                .orElse(15) + 2;

        // Print header
        System.out.printf("|%-" + nameLength + "s|%-" + ageLength + "s|%-" + regDateLength + "s|%n",
                "NAME", "AGE", "REGISTRATION DATE");

        // Print data
        for (StudentDTO studentDTO : resultList) {
            System.out.println("-".repeat(nameLength + ageLength + regDateLength + 4));
            System.out.printf("|%-" + nameLength + "s|%-" + ageLength + "d|%-" + regDateLength + "s|%n",
                    studentDTO.getName(),
                    studentDTO.getAge(),
                    studentDTO.getRegistrationDate());
        }
    }
}
