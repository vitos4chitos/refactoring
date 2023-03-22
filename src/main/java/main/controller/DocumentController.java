package main.controller;


import main.database.entity.*;
import main.database.service.DocumentAgregatorService;
import main.database.service.entity_service.*;
import main.entity.request.DocumentToAdd;
import main.entity.request.PreferentialDocument;
import main.entity.responce.BaseAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    ResponseEntity<Document> getDocumentById(@PathVariable("id") Long id) {
        return documentService.getDocument(id);
    }

    @GetMapping("status/{id}")
    ResponseEntity<Status> getStatusById(@PathVariable("id") Long id) {
        return statusService.getStatus(id);
    }

    @GetMapping("/type/{id}")
    ResponseEntity<TypeOfDocument> getTypeOfDocumentById(@PathVariable("id") Long id) {
        return typeOfDocumentService.getTypeOfDocument(id);
    }

    @GetMapping("/signature/{id}")
    ResponseEntity<Signature> getSignaturesById(@PathVariable("id") Long id) {
        return signaturesService.getSignature(id);
    }

    @GetMapping("/privileges/{id}")
    ResponseEntity<Privileges> getPrivilegesByDocumentId(@PathVariable("id") Long id) {
        return privilegesService.getPrivilege(id);
    }

    @GetMapping("/parameter/{id}")
    ResponseEntity<Parameter> getParameterByDocumentId(@PathVariable("id") Long id) {
        return parameterService.getParameter(id);
    }

    @GetMapping()
    ResponseEntity<BaseAnswer> getUsersDocuments(@RequestParam("login") String login) {
        return documentAgregatorService.getUsersDocuments(login);
    }

    @GetMapping("/{id}/info")
    ResponseEntity<BaseAnswer> getDockInfoByDocumentId(@PathVariable("id") Long id) {
        return documentAgregatorService.getDockInfoByDocumentId(id);
    }

    @GetMapping("/{id}/TypeOfDocumentWhichNotExist")
    ResponseEntity<List<TypeOfDocument>> getTypeOfDocumentByUserIdWhichNotExist(@PathVariable("id") Long id) {
        return documentAgregatorService.getNameTypeOfDocumentsWhichNotExistInDocuments(id);
    }

    @PostMapping()
    ResponseEntity<BaseAnswer> addDocument(@RequestBody DocumentToAdd document) {
        return documentAgregatorService.addDocument(document);
    }

    @GetMapping("{login}/reference")
    ResponseEntity<BaseAnswer> getAllReference(@PathVariable("login") String login) {
        return documentAgregatorService.getAllReference(login);
    }

    @PostMapping("/document-priority")
    ResponseEntity<BaseAnswer> addDocumentPrior(@RequestBody PreferentialDocument document) {
        return documentAgregatorService.addDocumentPrior(document);
    }


    @PostMapping("{id}/review")
    ResponseEntity<BaseAnswer> addForReviewByParameterId(@PathVariable("id") Long parameterId) {
        return documentService.addForReview(parameterId);
    }

}
