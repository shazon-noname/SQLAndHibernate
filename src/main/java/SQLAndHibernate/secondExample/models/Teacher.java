package SQLAndHibernate.secondExample.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Teachers")
@Data
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int salary;
    private int age;
}
