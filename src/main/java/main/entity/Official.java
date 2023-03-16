package main.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Time;
import java.util.Objects;

@Entity(name = "official")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Official {
    @Id
    @Column(name = "official_id")
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "time_of_receipts")
    private Time timeOfReceipts;

    @Column(name = "instance_id")
    private Long instanceId;

    @Column(name = "schedule_id")
    private Long scheduleId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Official official = (Official) o;
        return id != null && Objects.equals(id, official.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
