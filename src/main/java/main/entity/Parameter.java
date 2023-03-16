package main.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "Parameters")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Parameter {
    @Id
    @Column(name = "parameters_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private Boolean status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Parameter parameter = (Parameter) o;
        return id != null && Objects.equals(id, parameter.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
