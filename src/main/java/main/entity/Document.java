package main.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity(name = "Document")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Document {

    @Id
    @Column(name = "document_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_of_document_id")
    private Long typeOfDocumentId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "date_of_issue")
    private Date dateOfIssue;

    @Column(name = "validity")
    private Date validity;

    @Column(name = "issued_by_whom")
    private String issuedByWhom;

    @Column(name = "parameters_id")
    private Long parameters_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Document document = (Document) o;
        return id != null && Objects.equals(id, document.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
