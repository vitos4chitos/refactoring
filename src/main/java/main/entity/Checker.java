package main.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Time;
import java.util.Objects;

@Entity(name = "checker")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Checker {
    @Id
    @Column(name = "checker_id")
    private Long id;

    @Column(name = "time")
    private Time time;

    @Column(name = "max_quantity")
    private Long maxQuantity;

    @Column(name = "prosecutor_id")
    private Long prosecutorId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Checker checker = (Checker) o;
        return id != null && Objects.equals(id, checker.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
