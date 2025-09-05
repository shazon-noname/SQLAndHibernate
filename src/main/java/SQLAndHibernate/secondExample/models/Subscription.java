package SQLAndHibernate.secondExample.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "Subscriptions")
@Data
public class Subscription {
    @EmbeddedId
    private SubscriptionKey id;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private Student studentId;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", insertable = false, updatable = false)
    private Course courseId;
    @Column(name = "subscription_date")
    private Date subscriptionDate;

}