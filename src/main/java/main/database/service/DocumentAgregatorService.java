package main.database.service;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.database.entity.*;
import main.database.service.entity_service.*;
import main.entity.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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

    public ResponseEntity<BaseAnswer> buyDocument(String login, Long bookkeepingId, String name) {
        log.info("Поступил запрос на покупку документа");
        BigDecimal userMoney, productionCost;
        if (userService.getUserByLogin(login).isPresent()) {
            User user = userService.getUserByLogin(login).get();
            userMoney = user.getMoney();
            TypeOfDocument typeOfDocument = typeOfDocumentService.getByName(name);
            Production production = productionService.getProductionByBookkeepingIdAndAndTypeOfDocumentId(bookkeepingId, typeOfDocument.getId());
            log.info("До" + production.getCost());
            log.info("Скидка" + documentService.calculateSale(user.getId()));
            productionCost = BigDecimal.valueOf(production.getCost().doubleValue() * ((100 - documentService.calculateSale(user.getId())) / 100.0));
            log.info("После" + productionCost);
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
                    document.setParametersId(parameterOne.getId());
                    documentService.save(document);

                    queueService.putInQueue(user.getId(), officials.get(0).getId());
                    queueService.putInQueue(user.getId(), officials.get(1).getId());
                    queueService.putInQueue(user.getId(), officials.get(2).getId());

                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }
        }
        log.error("Пользователь не найден");
        return new ResponseEntity<>(new ErrorAnswer("UserNotFound"), HttpStatus.UNPROCESSABLE_ENTITY);

    }

    public ResponseEntity<BaseAnswer> getDocuments(String login) {
        long id = userService.getUserId(login);
        if(id == -1){
            log.error("Пользователь не найден");
            return new ResponseEntity<>(new ErrorAnswer("UserNotFound"), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        List<Document> documents = documentService.getAllDocumentsByUserId(id);
        long inst = userService.getUserById(id).getInstanceId();
        List<TypeOfDocument> typeOfDocuments = typeOfDocumentService.getTypes(inst);
        BackVals backVals = new BackVals();
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
                    BackVals.Vals bv = BackVals.Vals.builder().build();
                    bv.setName(typeOfDocument.getName());
                    backVals.addVal(bv);
                }
            }
        }
        return new ResponseEntity<>(backVals, HttpStatus.OK);
    }

    public ResponseEntity<BaseAnswer> getUsersDocuments(String login) {
        log.info("Поступил запрос на получение документов пользователя login = {}", login);
        long userId = userService.getUserId(login);
        if(userId == -1){
            log.error("Пользователь не найден");
            return new ResponseEntity<>(new ErrorAnswer("UserNotFound"), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        List<Document> documents = documentService.getAllDocumentsByUserId(userId);
        if (documents.isEmpty()){
            return new ResponseEntity<>(new BackVals(), HttpStatus.OK);
        }
        BackVals backVals = BackVals.builder().valsList(new ArrayList<>()).build();
        documents.forEach(d -> {
                TypeOfDocument typeOfDocument = typeOfDocumentService.getById(d.getTypeOfDocumentId());
                backVals.addVal(
                        BackVals.Vals.builder()
                                .id(d.getId())
                                .validity(d.getValidity())
                                .issue(d.getDateOfIssue())
                                .name(typeOfDocument.getName())
                                .bywhom(d.getIssuedByWhom())
                                .build()
                );
        });
        return new ResponseEntity<>(backVals, HttpStatus.OK);
    }

    public ResponseEntity<BaseAnswer> getDockInfoByDocumentId(Long id) {
        log.info("Поступил запрос на поиск информации о документе id = {}", id);
        log.info("Получаю информацию о документе");
        Document document = documentService.getDocumentById(id);
        if(document == null){
            log.error("Документ не найден");
            return new ResponseEntity<>(new ErrorAnswer("DocumentNotFound"), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        TypeOfDocument typeOfDocument = typeOfDocumentService.getById(document.getTypeOfDocumentId());
        Privileges privileges = privilegesService.getPrivilegesById(typeOfDocument.getPrivilegesId());
        String lgot = "Скидка = " + privileges.getSale() + "\n" + "Приоритет = " + privileges.getPriority() + "\n";
        log.info("Формирую базовую информацию о документе");
        DockInfo dockInfo = DockInfo.builder()
                .id(document.getId())
                .byWho(document.getIssuedByWhom())
                .issue(document.getDateOfIssue())
                .validity(document.getValidity())
                .name(typeOfDocument.getName())
                .lgot(lgot)
                .build();
        log.info("Формирую подписи документа");
        if (document.getParametersId() == null) {
            dockInfo.setSignatures(new ArrayList<>());
            dockInfo.setStatus(null);
        } else {
            List<Signature> signatures = signaturesService.getSignsById(document.getParametersId());
            dockInfo.setSignatures(signatures);
            Status status = statusService.getStatusByParamId(document.getParametersId());
            dockInfo.setStatus(status);

        }
        return new ResponseEntity<>(dockInfo, HttpStatus.OK);
    }

    public ResponseEntity<List<TypeOfDocument>> getNameTypeOfDocumentsWhichNotExistInDocuments(Long userId) {
        log.info("Поступил запрос на поиск недостающих документов у пользователя id = {}", userId);
        User user = userService.getUserById(userId);
        if(user == null){
            log.error("Пользователь не найден");
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        List<TypeOfDocument> typeOfDocuments = typeOfDocumentService
                .getAllTypeOfDocumentByInstanceId(user.getInstanceId());
        List<TypeOfDocument> ans = new ArrayList<>();
        for (TypeOfDocument td : typeOfDocuments) {
            boolean flag = documentService.existsDocumentByTypeOfDocumentIdAndUserId(td.getId(), userId);
            System.out.println(flag);
            if (!flag) {
                ans.add(td);
            }
        }
        return new ResponseEntity<>(typeOfDocuments.stream()
                .filter(td -> documentService.existsDocumentByTypeOfDocumentIdAndUserId(td.getId(), userId))
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<BaseAnswer> addDocument(DockumentToAdd document) {
        log.info("Поступил запрос на сохранение документа {}", document);
        long userId = userService.getUserId(document.getLogin());
        if(userId == -1){
            return new ResponseEntity<>(new ErrorAnswer("UserNotFound"), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        long typeId = -1;
        List<TypeOfDocument> somelist = typeOfDocumentService.getTypes(1);
        for (TypeOfDocument typeOfDocument : somelist) {
            if (typeOfDocument.getName().equals(document.getName()))
                typeId = typeOfDocument.getId();
        }
        Boolean status = documentService.addDocument(Document.builder()
                        .dateOfIssue(document.getDate1())
                        .issuedByWhom(document.getBywhom())
                        .parametersId(null)
                        .userId(userId)
                        .validity(document.getDate2())
                        .typeOfDocumentId(typeId)
                        .build());
        if(status){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new ErrorAnswer("SystemError"), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    public ResponseEntity<BaseAnswer> getAllReference(String login) {
        log.info("Поступил запрос getAllReference для пользователя login = {}", login);
        long userId = userService.getUserId(login);
        if(userId == -1){
            return new ResponseEntity<>(new ErrorAnswer("UserNotFound"), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        References references = new References();
        List<Document> documents = documentService.getAllDocumentsByUserId(userId);
        documents.stream().filter(document -> document.getParametersId() != null).forEach(document -> {
            Parameter parameter = parameterService.getByParameterId(document.getParametersId());
            TypeOfDocument typeOfDocument = typeOfDocumentService.getById(document.getTypeOfDocumentId());
            References.Reference reference = new References.Reference();
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
            references.addReference(reference);
            System.out.println(reference.getName());
        });
        return new ResponseEntity<>(references, HttpStatus.OK);
    }

    public ResponseEntity<BaseAnswer> addDocumentPrior(DocumentToAddPrior document) {
        log.info("Поступил запрос на добавление льготного документа {}", document);
        long userId = userService.getUserId(document.getLogin());
        if(userId == -1){
            return new ResponseEntity<>(new ErrorAnswer("UserNotFound"), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        log.info("Приступаю к заполнению полей");
        Document documentToAdd = new Document();
        TypeOfDocument typeOfDocument = new TypeOfDocument();
        Privileges privileges = new Privileges();
        privileges.setCoeffSign((long) 1);
        privileges.setPriority(document.getPrior());
        privileges.setSale(document.getSale());
        typeOfDocument.setInstanceId((long) 1);
        typeOfDocument.setName(document.getName());
        long id3 = -1;
        List<Privileges> priv = privilegesService.getAll(document.getSale());
        for (Privileges priveleg : priv) {
            if (priveleg.getPriority() == document.getPrior() && privileges.getSale() == document.getSale())
                id3 = priveleg.getId();
        }
        if (id3 == -1) {
            privilegesService.addPrivileges(privileges);
            List<Privileges> privv = privilegesService.getAll(document.getSale());
            for (Privileges priveleg : privv) {
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
        if (id2 == -1) {
            typeOfDocumentService.addTypeOfDocument(typeOfDocument);
            List<TypeOfDocument> somelistt = typeOfDocumentService.getTypes(1);
            for (TypeOfDocument typeOfDocuments : somelistt) {
                if (typeOfDocuments.getName().equals(document.getName()))
                    id2 = typeOfDocuments.getId();
            }
        }
        documentToAdd.setTypeOfDocumentId(id2);
        documentToAdd.setUserId(userId);
        documentToAdd.setDateOfIssue(document.getDate1());
        documentToAdd.setIssuedByWhom(document.getBywhom());
        documentToAdd.setParametersId(null);
        Boolean status = documentService.addDocument(documentToAdd);
        if(status){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new ErrorAnswer("SystemError"), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
