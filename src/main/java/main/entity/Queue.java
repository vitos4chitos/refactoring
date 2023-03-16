package main.entity;


import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity(name = "Queue")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Queue {
    @Id
    @Column(name = "queue_id")
    private Long id;

    @Column(name = "official_id")
    private Long officialId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "place")
    private Long place;

    @Column(name = "priority")
    private Long priority;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Queue queue = (Queue) o;
        return id != null && Objects.equals(id, queue.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
