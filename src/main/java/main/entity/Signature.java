package main.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "signatures")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Signature {
    @Id
    @Column(name = "signature_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "official_id")
    private Long officialId;

    @Column(name = "parameters_id")
    private Long parametersId;

    @Column(name = "is_subscribed")
    private Boolean isSubscribed;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Signature signature = (Signature) o;
        return id != null && Objects.equals(id, signature.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
