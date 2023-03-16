package main.controller;

import main.database.service.DocumentAgregatorService;
import main.database.service.ShopAgregatorService;
import main.database.service.entity_service.BookkeepingService;
import main.database.service.entity_service.DocumentService;
import main.entity.BackVals;
import main.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bkp")
public class BookeepingController {
    @Autowired
    DocumentService documentService;
    @Autowired
    BookkeepingService bookkeepingService;
    @Autowired
    DocumentAgregatorService documentAgregatorService;
    @Autowired
    ShopAgregatorService shopAgregatorService;

    @GetMapping("/{login}")
    List<BackVals> getDocuments(@PathVariable("login") String login) {
        return documentAgregatorService.getDocuments(login);
    }


    @PutMapping("/buy")
    public boolean buyDocument(@RequestParam String login, @RequestParam Long bookkeepingId, @RequestParam String name) {
        return documentAgregatorService.buyDocument(login, bookkeepingId, name);
    }

    @GetMapping("/shop/{document-name}")
    List<Shop> getShopsByDocumentName(@PathVariable("document-name") String name){
       return shopAgregatorService.getShopsByDocumentName(name);
    }

    @GetMapping()
    String getBookkeeping(@RequestParam Long id) {
        return bookkeepingService.getBookkeepingById(id).toString();
    }
}
