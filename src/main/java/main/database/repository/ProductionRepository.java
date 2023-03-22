package main.database.repository;

import main.database.entity.Production;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductionRepository extends JpaRepository<Production, Long> {
    Production getProductionByBookkeepingIdAndAndTypeOfDocumentId(Long bookkeepingId, Long documentId);

    List<Production> getProductionsByTypeOfDocumentId(Long id);
}
