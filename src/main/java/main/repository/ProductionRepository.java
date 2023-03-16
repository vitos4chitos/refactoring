package main.repository;

import main.entity.Production;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

public interface ProductionRepository extends JpaRepository<Production, Long> {
    Production getProductionById(Long id);
    Production getProductionByBookkeepingIdAndAndTypeOfDocumentId(Long bookkeepingId, Long documentId);
    List<Production> getProductionsByTypeOfDocumentId(Long id);
}
