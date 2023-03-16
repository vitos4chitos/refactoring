package main.controller;


import main.database.entity.*;
import main.database.service.DocumentAgregatorService;
import main.database.service.entity_service.*;
import main.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Documented;
import java.util.List;

@RestController
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    DocumentService documentService;
    @Autowired
    StatusService statusService;
    @Autowired
    TypeOfDocumentService typeOfDocumentService;
    @Autowired
    SignaturesService signaturesService;
    @Autowired
    ProductionService productionService;
    @Autowired
    PrivilegesService privilegesService;

    @Autowired
    ParameterService parameterService;

    @Autowired
    DocumentAgregatorService documentAgregatorService;


    @GetMapping("{id}")
    String getDocumentById(@PathVariable("id") Long id) {
        return documentService.getDocumentById(id).toString();
    }

    @GetMapping("status/{id}")
    String getStatusById(@PathVariable("id") Long id) {
        return statusService.getStatusById(id).toString();
    }

    @GetMapping("/type/{id}")
    String getTypeOfDocumentById(@PathVariable("id") Long id) {
        return typeOfDocumentService.getById(id).toString();
    }

    @GetMapping("/signature/{id}")
    String getSignaturesById(@PathVariable("id") Long id) {
        return signaturesService.getSignatureById(id).toString();
    }


    @GetMapping("/producation/{id}")
    String getProductionByDocumentId(@PathVariable("id") Long id) {
        return productionService.getProductionById(id).toString();
    }

    @GetMapping("/privileges/{id}")
    String getPrivilegesByDocumentId(@PathVariable("id")  Long id) {
        return privilegesService.getPrivilegesById(id).toString();
    }

    @GetMapping("/parameter/{id}")
    String getParameterByDocumentId(@PathVariable("id") Long id) {
        return parameterService.getByParameterId(id).toString();
    }

    @GetMapping()
    List<BackVals> getUsersDocuments(@RequestParam("login") String login) {
       return documentAgregatorService.getUsersDocuments(login);
    }

    @GetMapping("/{id}/info")
    DockInfo getDockInfoByDocumentId(@RequestParam("id") Long id){
        return documentAgregatorService.getDockInfoByDocumentId(id);
    }

    @GetMapping("/{id}/TypeOfDocumentWhichNotExist")
    List<TypeOfDocument> getTypeOfDocumentByUserIdWhichNotExist(@PathVariable("id") Long id) {
        return documentAgregatorService.getNameTypeOfDocumentsWhichNotExistInDocuments(id);
    }

    @PostMapping ()
    String addDocument(@RequestBody DockumentToAdd document){
        return documentAgregatorService.addDocument(document);
    }

    @GetMapping("{login}/reference}")
    List<Reference> getAllReference(@PathVariable("login") String login){
        return documentAgregatorService.getAllReference(login);
    }
    @PostMapping ("/document-priority")
    String addDocumentPrior(@RequestBody DocumentToAddPrior document){
        return documentAgregatorService.addDocumentPrior(document);
    }


    @PostMapping("{id}/review")
    public String addForReview(@PathVariable("id") Long parameterId) {
        return documentService.addForReview(parameterId);
    }

}
