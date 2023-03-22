package main.controller;

import main.database.entity.Bookkeeping;
import main.database.service.DocumentAgregatorService;
import main.database.service.ShopAgregatorService;
import main.database.service.entity_service.BookkeepingService;
import main.database.service.entity_service.DocumentService;
import main.entity.responce.BaseAnswer;
import main.entity.responce.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<BaseAnswer> getDocuments(@PathVariable("login") String login) {
        return documentAgregatorService.getDocuments(login);
    }

    @PostMapping("/buy")
    ResponseEntity<BaseAnswer> buyDocument(@RequestParam String login, @RequestParam Long bookkeepingId, @RequestParam String name) {
        return documentAgregatorService.buyDocument(login, bookkeepingId, name);
    }

    @GetMapping("/shop/{document-name}")
    ResponseEntity<List<Shop>> getShopsByDocumentName(@PathVariable("document-name") String name) {
        return shopAgregatorService.getShopsByDocumentName(name);
    }

    @GetMapping()
    ResponseEntity<Bookkeeping> getBookkeeping(@RequestParam Long id) {
        return bookkeepingService.getBookkeepingById(id);
    }
}
