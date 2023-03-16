package main.entity;


import com.vladmihalcea.hibernate.type.interval.PostgreSQLIntervalType;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.TypeDef;
import org.postgresql.util.PGInterval;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.sql.Time;
import java.time.Duration;
import java.util.Objects;

@Entity(name = "Production")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@TypeDef(
        typeClass = PostgreSQLIntervalType.class,
        defaultForType = Duration.class
)
public class Production {
    @Id
    @Column(name = "production_id")
    private Long id;

    @Column(name = "bookkeeping_id")
    private Long bookkeepingId;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "type_of_document_id")
    private Long typeOfDocumentId;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "time")
    private Duration time;


    // TODO: 15.03.2022 fix return null time interval 
    public Duration getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Production that = (Production) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
