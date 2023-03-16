package main.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "Privileges")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Privileges {
    @Id
    @Column(name = "privileges_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sale")
    private int sale;

    @Column(name = "priority")
    private Long priority;

    @Column(name = "coeff_sign")
    private Long coeffSign;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Privileges that = (Privileges) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
