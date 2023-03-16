package main.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity(name = "Prosecutor")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Prosecutor {

    @Id
    @Column(name = "prosecutor_id")
    private Long id;

    @Column(name = "instance_id")
    private Long instanceId;

    @Column(name = "schedule_id")
    private Long scheduleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Prosecutor that = (Prosecutor) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
