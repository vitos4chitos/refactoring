package main.entity;


import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity(name = "Status")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Status {

    @Id
    @Column(name = "status_id")
    private Long id;

    @Column(name = "is_valid")
    private Boolean isValid;

    @Column(name = "parameter_id")
    private Long parameterId;

    @Column(name = "checker_id")
    private Long checkerId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Status status = (Status) o;
        return id != null && Objects.equals(id, status.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
