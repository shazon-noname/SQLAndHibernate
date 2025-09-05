// src/main/java/SQLAndHibernate/secondExample/models/LinkedPurchaseListKey.java
package SQLAndHibernate.secondExample.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class LinkedPurchaseListKey implements Serializable {
    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "course_id")
    private Integer courseId;

}