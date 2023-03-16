package main.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Time;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "Schedule")
public class Schedule {

    @Id
    @Column(name = "schedule_id")
    private Long id;

    @Column(name = "time_start")
    private Time timeStart;

    @Column(name = "time_finish")
    private Time timeFinish;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Schedule schedule = (Schedule) o;
        return id != null && Objects.equals(id, schedule.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
