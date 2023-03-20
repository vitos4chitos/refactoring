package main.database.repository;

import main.database.entity.TypeOfDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TypeOfDocumentRepository extends JpaRepository<TypeOfDocument, Long> {
    TypeOfDocument getTypeOfDocumentById(Long id);

    TypeOfDocument getTypeOfDocumentByName(String name);

    List<TypeOfDocument> getTypeOfDocumentsByInstanceId(Long instanceId);
}
