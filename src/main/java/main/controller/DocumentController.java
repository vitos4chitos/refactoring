package main.controller;


import main.entity.*;
import main.repository.DocumentRepository;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    DocumentService documentService;

    @Autowired
    UserService userService;

    @Autowired
    TypeOfDocumentService typeOfDocumentService;

    @Autowired
    PrivilegesService privilegesService;

    @Autowired
    SignaturesService signaturesService;

    @Autowired
    OfficialService officialService;

    @Autowired
    StatusService statusService;

    @Autowired
    ParameterService parameterService;


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
    List<BackVals> getUsersDocuments(@RequestParam("login") String id) {
        long idd = userService.getUserId(id);
        List<Document> documents = documentService.getAllDocumentsByUserId(idd);
        List<BackVals> backVals = new ArrayList<>();
        if (documents != null) {
            for(int i = 0; i < documents.size(); i++){
                BackVals bv = new BackVals();
                bv.setId(documents.get(i).getId());
                TypeOfDocument typeOfDocument = typeOfDocumentService.getById(documents.get(i).getTypeOfDocumentId());
                bv.setValidity(documents.get(i).getValidity());
                bv.setIssue(documents.get(i).getDateOfIssue());
                bv.setName(typeOfDocument.getName());
                bv.setBywhom(documents.get(i).getIssuedByWhom());
                backVals.add(bv);
            }
            return backVals;
        }
        return backVals;
    }

    @GetMapping("/{id}/info")
    DockInfo getDockInfoByDocumentId(@RequestParam("id") Long id){
        System.out.println(id);
        DockInfo dockInfo = new DockInfo();
        Document document = documentService.getDocumentById(id);
        dockInfo.setId(document.getId());
        dockInfo.setByWho(document.getIssuedByWhom());
        dockInfo.setIssue(document.getDateOfIssue());
        dockInfo.setValidity(document.getValidity());
        TypeOfDocument typeOfDocument = typeOfDocumentService.getById(document.getTypeOfDocumentId());
        dockInfo.setName(typeOfDocument.getName());
        String lgot = "";
        Privileges privileges = privilegesService.getPrivilegesById(typeOfDocument.getPrivilegesId());
        lgot += "Скидка = " + privileges.getSale() + "\n" + "Приоритет = " + privileges.getPriority() + "\n";
        dockInfo.setLgot(lgot);
        if(document.getParameters_id() == null){
            dockInfo.setPod("Подписи не требуются");
            dockInfo.setPodtver("Подтверждения не требуется");
        }
        else{
            String pod = "";
            List<Signature> signatures = signaturesService.getSignsById(document.getParameters_id());
            Official official;
            for (Signature signature : signatures) {
                official = officialService.getOfficialById(signature.getOfficialId());
                pod += official.getSurname() + " " + official.getName();
                if (signature.getIsSubscribed()) {
                    pod += " подписал";
                } else {
                    pod += " не подписал";
                }
                pod += "\n";
            }
            dockInfo.setPod(pod);
            String podtver = "";
            Status status = statusService.getStatusByParamId(document.getParameters_id());
            if(status != null && status.getIsValid()){
                podtver = "Все четко";
            }
            else{
                podtver = "Не подтверждено";
            }
            dockInfo.setPodtver(podtver);

        }
        return dockInfo;
    }

    @GetMapping("/{id}/TypeOfDocumentWhichNotExist")
    List<TypeOfDocument> getTypeOfDocumentByUserIdWhichNotExist(@PathVariable("id") Long id) {
        return documentService.getNameTypeOfDocumentsWhichNotExistInDocuments(id);
    }

    @PostMapping ()
    String addDocument(@RequestBody DockumentToAdd document){
        Document document1 = new Document();
        long id = userService.getUserId(document.getLogin());
        long id2 = -1;
        System.out.println(id);
        document1.setDateOfIssue(document.getDate1());
        System.out.println(document.getName());
        document1.setIssuedByWhom(document.getBywhom());
        document1.setParameters_id(null);
        document1.setUserId(id);
        document1.setValidity(document.getDate2());
        List<TypeOfDocument> somelist = typeOfDocumentService.getTypes(1);
        for (TypeOfDocument typeOfDocument : somelist) {
            if (typeOfDocument.getName().equals(document.getName()))
                id2 = typeOfDocument.getId();
        }
        document1.setTypeOfDocumentId(id2);
        return documentService.addDocument(document1);
    }

    @GetMapping("{login}/reference}")
    List<Reference> getAllReference(@PathVariable("login") String login){
        System.out.println(login);
        long idd = userService.getUserId(login);
        List<Reference> references = new ArrayList<>();
        List<Document> documents = documentService.getAllDocumentsByUserId(idd);
        for (Document document : documents) {
            System.out.println(document.getId());
            if (document.getParameters_id() != null) {
                Parameter parameter = parameterService.getByParametrId(document.getParameters_id());
                TypeOfDocument typeOfDocument = typeOfDocumentService.getById(document.getTypeOfDocumentId());
                Reference reference = new Reference();
                reference.setId(parameter.getId());
                reference.setName(typeOfDocument.getName());
                if (parameter.getStatus()) {
                    reference.setSign("Подписано");
                    Status status = statusService.getStatusByParamId(parameter.getId());
                    if (status != null) {
                        if (status.getIsValid()) {
                            reference.setCheck("Проверено");
                        } else {
                            reference.setCheck("Не проверено");
                        }
                    } else {
                        reference.setCheck("Не добавлено на проверку");
                    }
                } else {
                    reference.setSign("Не подписано");
                    reference.setCheck("Не добавлено проверено");
                }
                references.add(reference);
                System.out.println(reference.getName());
            }
        }
        return references;
    }
    @PostMapping ("/document-priority")
    String addDocumentPrior(@RequestBody DocumentToAddPrior document){
        long id1 = -1;
        long id = userService.getUserId(document.getLogin());
        Document document1 = new Document();
        TypeOfDocument typeOfDocument = new TypeOfDocument();
        Privileges privileges = new Privileges();
        privileges.setCoeffSign((long) 1);
        privileges.setPriority(document.getPrior());
        privileges.setSale(document.getSale());
        typeOfDocument.setInstanceId((long) 1);
        typeOfDocument.setName(document.getName());
        long id3 = -1;
        List<Privileges> priv = privilegesService.getAll(document.getSale());
        for (Privileges  priveleg : priv) {
            if (priveleg.getPriority() == document.getPrior() && privileges.getSale() == document.getSale())
                id3 = priveleg.getId();
        }
        if(id3 == -1){
            privilegesService.addPrivileges(privileges);
            List<Privileges> privv = privilegesService.getAll(document.getSale());
            for (Privileges  priveleg : privv) {
                if (priveleg.getPriority() == document.getPrior() && privileges.getSale() == document.getSale())
                    id3 = priveleg.getId();
            }
        }
        typeOfDocument.setPrivilegesId(id3);
        long id2 = -1;
        List<TypeOfDocument> somelist = typeOfDocumentService.getTypes(1);
        for (TypeOfDocument typeOfDocuments : somelist) {
            if (typeOfDocuments.getName().equals(document.getName()))
                id2 = typeOfDocuments.getId();
        }
        if(id2 == -1){
            typeOfDocumentService.addTypeOfDocument(typeOfDocument);
            List<TypeOfDocument> somelistt = typeOfDocumentService.getTypes(1);
            for (TypeOfDocument typeOfDocuments : somelistt) {
                if (typeOfDocuments.getName().equals(document.getName()))
                    id2 = typeOfDocuments.getId();
            }
        }
        document1.setTypeOfDocumentId(id2);
        document1.setUserId(id);
        document1.setDateOfIssue(document.getDate1());
        System.out.println(document.getName());
        document1.setIssuedByWhom(document.getBywhom());
        document1.setParameters_id(null);
        return documentService.addDocument(document1);
    }


    @PostMapping("{id}/review")
    public String addForReview(@PathVariable("id") Long parameterId) {
        documentService.addForReview(parameterId);
        return "{\"token\": \"true\"}";
    }




}
