package main.controller;


import main.database.entity.*;
import main.database.service.*;
import main.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    DocumentService documentService;


    @GetMapping("{id}")
    String getDocumentById(@PathVariable("id") Long id) {
        return documentService.getDocumentById(id).toString();
    }

    @GetMapping("/{id}/status")
    String getStatusOfDocumentById(@PathVariable("id") Long id) {
        return documentService.getStatusById(id).toString();
    }

    @GetMapping("/{id}/type")
    String getTypeOfDocumentById(@PathVariable("id") Long id) {
        return documentService.getTypeOfDocumentId(id).toString();
    }

    @GetMapping("/{id}/signatures")
    String getSignaturesByDocumentId(@PathVariable("id") Long id) {
        return documentService.getSignatureById(id).toString();
    }


    @GetMapping("/{id}/production")
    String getProductionByDocumentId(@PathVariable("id") Long id) {
        return documentService.getProductionById(id).toString();
    }

    @GetMapping("/{id}/privileges")
    String getPrivilegesByDocumentId(@PathVariable("id")  Long id) {
        return documentService.getPrivilegesById(id).toString();
    }

    @GetMapping("/{id}/parameter")
    String getParameterByDocumentId(@PathVariable("id") Long id) {
        return documentService.getParameterById(id).toString();
    }

    @GetMapping()
    List<BackVals> getUsersDocuments(@RequestParam("login") String login) {
       return documentService.getUsersDocuments(login);
    }

    @GetMapping("/{id}/info")
    DockInfo getDockInfoByDocumentId(@RequestParam("id") Long id){
        return documentService.getDockInfoByDocumentId(id);
    }

    @GetMapping("/{id}/TypeOfDocumentWhichNotExist")
    List<TypeOfDocument> getTypeOfDocumentByUserIdWhichNotExist(@PathVariable("id") Long id) {
        return documentService.getNameTypeOfDocumentsWhichNotExistInDocuments(id);
    }

    @PostMapping ()
    String addDocument(@RequestBody DockumentToAdd document){
        return documentService.addDocument(document);
    }

    @GetMapping("{login}/reference}")
    List<Reference> getAllReference(@PathVariable("login") String login){
        return documentService.getAllReference(login);
    }
    @PostMapping ("/document-priority")
    String addDocumentPrior(@RequestBody DocumentToAddPrior document){
        return documentService.addDocumentPrior(document);
    }


    @PostMapping("{id}/review")
    public String addForReview(@PathVariable("id") Long parameterId) {
        return documentService.addForReview(parameterId);
    }

}
