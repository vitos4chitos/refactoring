package main.database.entity;


import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity(name = "bookkeeping")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Bookkeeping {
    @Id
    @Column(name = "bookkeeping_id")
    private Long id;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "schedule_id")
    private Long scheduleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Bookkeeping that = (Bookkeeping) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
