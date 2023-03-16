package main.repository;

import main.entity.TypeOfDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TypeOfDocumentRepository extends JpaRepository<TypeOfDocument, Long> {
    TypeOfDocument getTypeOfDocumentById(Long id);
    TypeOfDocument getTypeOfDocumentByName(String name);
    List<TypeOfDocument> getTypeOfDocumentsByInstanceId(Long instanceId);
}
