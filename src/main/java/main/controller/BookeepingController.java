package main.controller;

import main.database.entity.Production;
import main.database.service.*;
import main.entity.BackVals;
import main.database.entity.Document;
import main.database.entity.TypeOfDocument;
import main.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bkp")
public class BookeepingController {
    @Autowired
    DocumentService documentService;
    @Autowired
    BookkeepingService bookkeepingService;

    @GetMapping("/{login}")
    List<BackVals> getDocuments(@PathVariable("login") String login) {
        return bookkeepingService.getDocuments(login);
    }


    @PutMapping("/buy")
    public boolean buyDocument(@RequestParam String login, @RequestParam Long bookkeepingId, @RequestParam String name) {
        return bookkeepingService.buyDocument(login, bookkeepingId, name);
    }

    @GetMapping("/shop/{document-name}")
    List<Shop> getShopsByDocumentName(@PathVariable("document-name") String name){
       return bookkeepingService.getShopsByDocumentName(name);
    }

    @GetMapping()
    String getBookkeeping(@RequestParam Long id) {
        return documentService.getBookkeepingById(id).toString();
    }
}
