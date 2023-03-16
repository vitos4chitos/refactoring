package main.controller;

import main.entity.BackVals;
import main.entity.Document;
import main.entity.TypeOfDocument;
import main.entity.*;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bkp")
public class BookeepingController {
    @Autowired
    UserService userService;

    @Autowired
    DocumentService documentService;

    @Autowired
    TypeOfDocumentService typeOfDocumentService;

    @Autowired
    BookkeepingService bookkeepingService;

    @Autowired
    ProductionService productionService;


    @GetMapping("/getAll")
    List<BackVals> getDocumentsById(@RequestParam("login") String login) {
        long id = userService.getUserId(login);
        List<Document> documents = documentService.getAllDocumentsByUserId(id);
        long inst = userService.getUserById(id).getInstanceId();
        List<TypeOfDocument> typeOfDocuments = typeOfDocumentService.getTypes(inst);
        List<BackVals> backVals = new ArrayList<>();
        for (TypeOfDocument typeOfDocument : typeOfDocuments) {
            if (typeOfDocument.getPrivilegesId() == 1 && !typeOfDocument.getName().equals("Пасспорт") &&
                    !typeOfDocument.getName().equals("Свидетельство о рождении") &&
                    !typeOfDocument.getName().equals("Права на вождение автомобиля") &&
                    !typeOfDocument.getName().equals("Справка с места работы")) {
                boolean flag = false;
                for (Document document : documents) {
                    if (document.getTypeOfDocumentId().equals(typeOfDocument.getId())) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    BackVals bv = new BackVals();
                    bv.setName(typeOfDocument.getName());
                    backVals.add(bv);
                }
            }
        }
        for(int i = 0; i < backVals.size(); i++){
            System.out.println(backVals.get(i).getName());
        }
        System.out.println();
        return backVals;
    }


    @GetMapping("/buy")
    public boolean buyDocument(@RequestParam String login, @RequestParam Long bookkeepingId, @RequestParam String name) {
        System.out.println(name + " " + login + " " + bookkeepingId);
        return bookkeepingService.buyDocument(login, bookkeepingId, name);
    }

    @GetMapping("/getShops")
    List<Shop> getShops(@RequestParam("name") String name){
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
