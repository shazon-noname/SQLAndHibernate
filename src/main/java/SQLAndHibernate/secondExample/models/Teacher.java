package SQLAndHibernate.secondExample.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Teachers")
@Data
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer salary;
    private Integer age;
}
