package SQLAndHibernate.secondExample.models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "LinkedPurchaseList")
@Data
public class LinkedPurchaseList {
    @EmbeddedId
    private LinkedPurchaseListKey id;
}