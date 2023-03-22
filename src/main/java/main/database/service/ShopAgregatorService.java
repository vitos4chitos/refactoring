package main.database.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.database.entity.Production;
import main.database.entity.TypeOfDocument;
import main.database.service.entity_service.*;
import main.entity.responce.Shop;
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
        if(!typeOfDocumentService.isPresentByName(name)){
            log.error("Такого документа не существует");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        TypeOfDocument typeOfDocument = typeOfDocumentService.getByName(name);
        List<Production> productions = productionService.getProductionByType(typeOfDocument.getId());
        List<Shop> shops = new ArrayList<>();
        log.info("Приступаю к заполнению информации о магазинах");
        for (Production production : productions) {
            Shop shop = Shop.builder()
                    .cost(production.getCost())
                    .shopId(production.getBookkeepingId())
                    .quantity(production.getQuantity())
                    .time(String.format
                            ("%d:%02d:%02d",
                                    production.getTime().getSeconds() / 3600,
                                    (production.getTime().getSeconds() % 3600) / 60,
                                    (production.getTime().getSeconds() % 60)
                            )
                    )
                    .build();
            shops.add(shop);
        }
        return new ResponseEntity<>(shops, HttpStatus.OK);
    }
}
