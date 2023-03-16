package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import main.database.entity.Production;
import main.database.repository.ProductionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductionService {

    private ProductionRepository productionRepository;

    public List<Production> getProductionByType(Long id) {

        return productionRepository.getProductionsByTypeOfDocumentId(id);
    }

    public Production getProductionByBookkeepingIdAndAndTypeOfDocumentId(Long bookkeepingId, Long typeOfDocumentId) {
        return productionRepository.getProductionByBookkeepingIdAndAndTypeOfDocumentId(bookkeepingId, typeOfDocumentId);

    }

    public void save(Production production){
        productionRepository.save(production);
    }

    public Production getProductionById(Long id) {
        return productionRepository.getProductionById(id);
    }
}
