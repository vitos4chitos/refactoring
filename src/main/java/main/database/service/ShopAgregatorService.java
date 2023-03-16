package main.database.service;

import lombok.RequiredArgsConstructor;
import main.database.entity.Production;
import main.database.entity.TypeOfDocument;
import main.database.service.entity_service.*;
import main.entity.Shop;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopAgregatorService {

    private final TypeOfDocumentService typeOfDocumentService;
    private final ProductionService productionService;

    public List<Shop> getShopsByDocumentName(String name){
        System.out.println(name);
        TypeOfDocument typeOfDocument = typeOfDocumentService.getByName(name);
        Long id = typeOfDocument.getId();
        System.out.println(id);
        List<Production> productions = productionService.getProductionByType(id);
        Shop shop;
        List<Shop> shops = new ArrayList<>();
        for (Production production : productions) {
            shop = new Shop();
            shop.setCost(production.getCost());
            shop.setId(production.getBookkeepingId());
            shop.setQuantity(production.getQuantity());
            System.out.println(production.getId());
            String s = String.format("%d:%02d:%02d",production.getTime().getSeconds() / 3600, (production.getTime().getSeconds() % 3600)
                    / 60, (production.getTime().getSeconds() % 60));
            System.out.println(s);
            shop.setTime(s);
            shops.add(shop);
        }
        return shops;
    }
}
