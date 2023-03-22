package main.database.repository;

import main.database.entity.Document;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Document getDocumentById(Long id);

    List<Document> getDocumentsByUserId(Long userId);

    Boolean existsDocumentByTypeOfDocumentIdAndUserId(Long typeOfDocumentId, Long userId);

    @Transactional
    @Procedure(procedureName = "calculate_sale")
    Long calculateSale(Long userId);

    @Transactional
    @Procedure(procedureName = "add_for_review")
    void addForReview(Long parameterId);

    boolean existsById(@NotNull Long id);
}
