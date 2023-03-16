package main.service;

import main.entity.Production;
import main.repository.ProductionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductionService {

    private ProductionRepository productionRepository;
    public  ProductionService(ProductionRepository productionRepository){
        this.productionRepository = productionRepository;
    }

    public List<Production> getProductionByType(Long id){

        return productionRepository.getProductionsByTypeOfDocumentId(id);
    }
}
