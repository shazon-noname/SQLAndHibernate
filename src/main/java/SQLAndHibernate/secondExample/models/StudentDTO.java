package SQLAndHibernate.secondExample.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
@Data
@AllArgsConstructor
public class StudentDTO {
    private String name;
    private Integer age;
    private Date registrationDate;
}
