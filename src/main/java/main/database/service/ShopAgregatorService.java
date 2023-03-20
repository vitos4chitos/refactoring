package main.database.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.database.entity.Production;
import main.database.entity.TypeOfDocument;
import main.database.service.entity_service.*;
import main.entity.Shop;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopAgregatorService {

    private final TypeOfDocumentService typeOfDocumentService;
    private final ProductionService productionService;

    public ResponseEntity<List<Shop>> getShopsByDocumentName(String name) {
        log.info("Поступил запрос на получение магазинов, где есть справка name = {}", name);
        TypeOfDocument typeOfDocument = typeOfDocumentService.getByName(name);
        Long id = typeOfDocument.getId();
        List<Production> productions = productionService.getProductionByType(id);
        Shop shop;
        List<Shop> shops = new ArrayList<>();
        for (Production production : productions) {
            shop = new Shop();
            shop.setCost(production.getCost());
            shop.setId(production.getBookkeepingId());
            shop.setQuantity(production.getQuantity());
            System.out.println(production.getId());
            String s = String.format("%d:%02d:%02d", production.getTime().getSeconds() / 3600, (production.getTime().getSeconds() % 3600)
                    / 60, (production.getTime().getSeconds() % 60));
            System.out.println(s);
            shop.setTime(s);
            shops.add(shop);
        }
        return new ResponseEntity<>(shops, HttpStatus.OK);
    }
}
