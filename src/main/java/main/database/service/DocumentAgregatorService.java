package main.database.service;

import lombok.RequiredArgsConstructor;
import main.database.entity.*;
import main.database.service.entity_service.*;
import main.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentAgregatorService {

    private final UserService userService;
    private final DocumentService documentService;
    private final TypeOfDocumentService typeOfDocumentService;
    private final ProductionService productionService;
    private final BookkeepingService bookkeepingService;
    private final ParameterService parameterService;
    private final OfficialService officialService;
    private final SignaturesService signatureService;
    private final QueueService queueService;
    private final PrivilegesService privilegesService;
    private final SignaturesService signaturesService;
    private final StatusService statusService;

    public boolean buyDocument(String login, Long bookkeepingId, String name) {
        Bookkeeping bookkeeping = bookkeepingService.getBookkeepingById(bookkeepingId);
        BigDecimal userMoney, productionCost;
        if (userService.getUserByLogin(login).isPresent()) {
            User user =userService.getUserByLogin(login).get();
            userMoney = user.getMoney();
            TypeOfDocument typeOfDocument = typeOfDocumentService.getByName(name);
            //System.out.println(bookkeepingId);
            //System.out.println(typeOfDocument.getId());
            //System.out.println(documentRepository.calculateSale(user.getId()));
            Production production = productionService.getProductionByBookkeepingIdAndAndTypeOfDocumentId(bookkeepingId, typeOfDocument.getId());
            System.out.println("До" + production.getCost());
            System.out.println("Скидка" + documentService.calculateSale(user.getId()));
            productionCost = BigDecimal.valueOf(production.getCost().doubleValue() * ((100 - documentService.calculateSale(user.getId())) / 100.0));
            System.out.println("После" + productionCost);
            if (userMoney.subtract(productionCost).doubleValue() >= 0) {
                Signature signatureOne = new Signature();
                Signature signatureTwo = new Signature();
                Signature signatureThree = new Signature();
                Parameter parameterOne = new Parameter();
                parameterOne.setStatus(false);

                user.setMoney(userMoney.subtract(productionCost));
                userService.save(user);

                parameterService.save(parameterOne);


                List<Official> officials = officialService.getOfficialByInstanceId(user.getInstanceId());
                System.out.println(officials.size());
                if (officials.size() >= 3) {
                    signatureOne.setIsSubscribed(false);
                    signatureOne.setOfficialId(officials.get(0).getId());
                    signatureOne.setParametersId(parameterOne.getId());

                    signatureTwo.setIsSubscribed(false);
                    signatureTwo.setOfficialId(officials.get(1).getId());
                    signatureTwo.setParametersId(parameterOne.getId());

                    signatureThree.setIsSubscribed(false);
                    signatureThree.setOfficialId(officials.get(2).getId());
                    signatureThree.setParametersId(parameterOne.getId());

                    signatureService.save(signatureOne);
                    signatureService.save(signatureTwo);
                    signatureService.save(signatureThree);

                    production.setQuantity(production.getQuantity() - 1);

                    productionService.save(production);

                    Document document = new Document();
                    document.setTypeOfDocumentId(typeOfDocument.getId());
                    java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                    document.setDateOfIssue(date);
                    document.setUserId(user.getId());
                    document.setValidity(null);
                    document.setIssuedByWhom("Замок");
                    document.setParameters_id(parameterOne.getId());
                    documentService.save(document);

                    queueService.putInQueue(user.getId(), officials.get(0).getId());
                    queueService.putInQueue(user.getId(), officials.get(1).getId());
                    queueService.putInQueue(user.getId(), officials.get(2).getId());

                    return true;
                }
            }
        }
        return false;

    }

    public List<BackVals> getDocuments(String login) {
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

    public List<BackVals> getUsersDocuments(String id) {
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

    public DockInfo getDockInfoByDocumentId(Long id){
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

    public List<TypeOfDocument> getNameTypeOfDocumentsWhichNotExistInDocuments(Long userId) {
        User user = userService.getUserById(userId);
        Long instanceId = user.getInstanceId();
        List<TypeOfDocument> typeOfDocuments = typeOfDocumentService.getAllTypeOfDocumentByInstanceId(instanceId);
        List<TypeOfDocument> ans = new ArrayList<>();
        for (TypeOfDocument td : typeOfDocuments) {
            boolean flag = documentService.existsDocumentByTypeOfDocumentIdAndUserId(td.getId(), userId);
            System.out.println(flag);
            if (!flag) {
                ans.add(td);
            }
        }
        return ans;
    }

    public String addDocument(DockumentToAdd document){
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

    public List<Reference> getAllReference(String login){
        System.out.println(login);
        long idd = userService.getUserId(login);
        List<Reference> references = new ArrayList<>();
        List<Document> documents = documentService.getAllDocumentsByUserId(idd);
        for (Document document : documents) {
            System.out.println(document.getId());
            if (document.getParameters_id() != null) {
                Parameter parameter = parameterService.getByParameterId(document.getParameters_id());
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

    public String addDocumentPrior(DocumentToAddPrior document){
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
}
