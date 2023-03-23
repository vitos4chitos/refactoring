package main.database.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.database.entity.*;
import main.database.service.entity_service.*;
import main.entity.request.DocumentToAdd;
import main.entity.request.PreferentialDocument;
import main.entity.responce.BaseAnswer;
import main.entity.responce.DockInfo;
import main.entity.responce.ErrorAnswer;
import main.entity.responce.back_documents.BackDocument;
import main.entity.responce.back_documents.BackDocuments;
import main.entity.responce.reference.Reference;
import main.entity.responce.reference.References;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
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
        Optional<User> optionalUser = userService.getUserByLogin(login);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            BigDecimal productionCost;
            TypeOfDocument typeOfDocument = typeOfDocumentService.getByName(name);
            Production production = productionService.getProductionByBookkeepingIdAndAndTypeOfDocumentId(bookkeepingId, typeOfDocument.getId());
            log.info("Цена до " + production.getCost());
            Long sale = documentService.calculateSale(user.getId());
            if(sale != null) {
                log.info("Скидка = " + sale);
                productionCost = BigDecimal.valueOf(production.getCost().doubleValue() * ((100 - sale) / 100.0));
                log.info("Цена послe " + productionCost);
            }
            else
                productionCost = BigDecimal.valueOf(production.getCost().doubleValue() * 100);
            if (user.getMoney().subtract(productionCost).doubleValue() >= 0) {
                Parameter parameterOne = Parameter.builder()
                        .status(false)
                        .build();
                user.setMoney(user.getMoney().subtract(productionCost));
                List<Official> officials = officialService.getOfficialByInstanceId(user.getInstanceId());
                log.info("Все должностные лица на участке: {}", officials);
                if (officials.size() >= 3) {
                    parameterService.save(parameterOne);
                    signatureService.save(List.of(
                            Signature.builder()
                                .isSubscribed(false)
                                .officialId(officials.get(0).getId())
                                .parametersId(parameterOne.getId())
                                .build(),
                            Signature.builder()
                                    .isSubscribed(false)
                                    .officialId(officials.get(1).getId())
                                    .parametersId(parameterOne.getId())
                                    .build(),
                            Signature.builder()
                                    .isSubscribed(false)
                                    .officialId(officials.get(2).getId())
                                    .parametersId(parameterOne.getId())
                                    .build()));
                    production.setQuantity(production.getQuantity() - 1);
                    productionService.save(production);
                    documentService.save(
                            Document.builder()
                                    .typeOfDocumentId(typeOfDocument.getId())
                                    .dateOfIssue(new java.sql.Date(Calendar.getInstance().getTime().getTime()))
                                    .userId(user.getId())
                                    .validity(null)
                                    .issuedByWhom("Замок")
                                    .parametersId(parameterOne.getId())
                                    .build()
                            );
                    queueService.putInQueue(user, officials);
                    userService.save(user);
                    parameterService.save(parameterOne);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
                else{
                    log.error("Недостаточно оф. лиц на участке");
                    return new ResponseEntity<>(new ErrorAnswer("NotMuchOfficials"), HttpStatus.BAD_REQUEST);
                }
            }
            else{
                log.error("Недостаточно средств на счету у пользователя");
                return new ResponseEntity<>(new ErrorAnswer("NotEnoughMoney"), HttpStatus.BAD_REQUEST);
            }
        }
        log.error("Пользователь не найден");
        return new ResponseEntity<>(new ErrorAnswer("UserNotFound"), HttpStatus.UNPROCESSABLE_ENTITY);

    }

    public ResponseEntity<BaseAnswer> getDocuments(String login) {
        log.info("Поступил запрос на получение документов на покупку для пользователя login = {}", login);
        long id = userService.getUserId(login);
        if(id == -1){
            log.error("Пользователь не найден");
            return new ResponseEntity<>(new ErrorAnswer("UserNotFound"), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        List<Document> documents = documentService.getAllDocumentsByUserId(id);
        if(documents.isEmpty()){
            log.info("Документы у пользователя отсутсвуют");
            return new ResponseEntity<>(new BackDocuments(), HttpStatus.OK);
        }
        User user = userService.getUserById(id);
        List<TypeOfDocument> typeOfDocuments = typeOfDocumentService.getTypes(user.getInstanceId());
        BackDocuments backVals = BackDocuments.builder().documentList(new ArrayList<>()).build();
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
                    BackDocument bv = BackDocument.builder().build();
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
            log.info("Документы не найдены. Отсутсвуют у пользователя");
            return new ResponseEntity<>(new BackDocuments(), HttpStatus.OK);
        }
        BackDocuments backVals = BackDocuments.builder().documentList(new ArrayList<>()).build();
        documents.forEach(d -> {
                TypeOfDocument typeOfDocument = typeOfDocumentService.getById(d.getTypeOfDocumentId());
                backVals.addVal(
                        BackDocument.builder()
                                .documentId(d.getId())
                                .validityDate(d.getValidity())
                                .issueDate(d.getDateOfIssue())
                                .name(typeOfDocument.getName())
                                .issuedBy(d.getIssuedByWhom())
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
                .documentId(document.getId())
                .isuedBy(document.getIssuedByWhom())
                .issueDate(document.getDateOfIssue())
                .validityDate(document.getValidity())
                .name(typeOfDocument.getName())
                .lgots(lgot)
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
        log.info("Начинаю поиск недостающих документов");
        List<TypeOfDocument> typeOfDocuments = typeOfDocumentService
                .getAllTypeOfDocumentByInstanceId(user.getInstanceId());
        return new ResponseEntity<>(typeOfDocuments.stream()
                .filter(td -> !documentService.existsDocumentByTypeOfDocumentIdAndUserId(td.getId(), userId))
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<BaseAnswer> addDocument(DocumentToAdd document) {
        log.info("Поступил запрос на сохранение документа {}", document);
        long userId = userService.getUserId(document.getUserLogin());
        if(userId == -1){
            return new ResponseEntity<>(new ErrorAnswer("UserNotFound"), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        long typeId = -1;
        List<TypeOfDocument> somelist = typeOfDocumentService.getTypes(1);
        for (TypeOfDocument typeOfDocument : somelist) {
            if (typeOfDocument.getName().equals(document.getName()))
                typeId = typeOfDocument.getId();
        }
        if(typeId == -1){
            log.error("Тип документа не найден");
            return new ResponseEntity<>(new ErrorAnswer("TypeOfDocumentNotFound"), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Boolean status = documentService.addDocument(Document.builder()
                        .dateOfIssue(document.getIssueDate())
                        .issuedByWhom(document.getIssuedBy())
                        .parametersId(null)
                        .userId(userId)
                        .validity(document.getValidityDate())
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
        References references = References.builder().references(new ArrayList<>()).build();
        List<Document> documents = documentService.getAllDocumentsByUserId(userId);
        if(documents.isEmpty()){
            log.info("Документы у пользователя отсутсвуют");
            return new ResponseEntity<>(references, HttpStatus.OK);
        }
        log.info("Приступаю к наполнению ответа");
        documents.stream().filter(document -> document.getParametersId() != null).forEach(document -> {
            Parameter parameter = parameterService.getByParameterId(document.getParametersId());
            TypeOfDocument typeOfDocument = typeOfDocumentService.getById(document.getTypeOfDocumentId());
            Reference reference = Reference.builder()
                    .id(parameter.getId())
                    .name(typeOfDocument.getName())
                    .build();
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
        });
        return new ResponseEntity<>(references, HttpStatus.OK);
    }

    public ResponseEntity<BaseAnswer> addDocumentPrior(PreferentialDocument document) {
        log.info("Поступил запрос на добавление льготного документа {}", document);
        long userId = userService.getUserId(document.getUserLogin());
        if(userId == -1){
            return new ResponseEntity<>(new ErrorAnswer("UserNotFound"), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        log.info("Приступаю к заполнению полей");
        Document documentToAdd = Document.builder()
                .userId(userId)
                .dateOfIssue(document.getIssueDate())
                .issuedByWhom(document.getIssuedBy())
                .parametersId(null)
                .build();
        TypeOfDocument typeOfDocument = TypeOfDocument.builder()
                .instanceId(1L)
                .name(document.getName())
                .build();
        Privileges privileges = Privileges.builder()
                .coeffSign(1L)
                .priority(document.getPriority())
                .sale(document.getSale())
                .build();
        List<Privileges> privilegesList = privilegesService.getAll(document.getSale());
        Optional<Privileges> optionalPrivileges = privilegesList.stream()
                .filter(p -> p.getPriority() == document.getPriority()).findAny();
        if (!optionalPrivileges.isPresent()) {
            privilegesService.addPrivileges(privileges);
            typeOfDocument.setPrivilegesId(privileges.getId());
        }
        else{
            typeOfDocument.setPrivilegesId(optionalPrivileges.get().getId());
        }
        List<TypeOfDocument> typeOfDocuments = typeOfDocumentService.getTypes(1);
        Optional<TypeOfDocument> optionalTypeOfDocument = typeOfDocuments.stream()
                .filter(t -> t.getName().equals(document.getName()))
                .findFirst();
        if (!optionalTypeOfDocument.isPresent()) {
            typeOfDocumentService.addTypeOfDocument(typeOfDocument);
            documentToAdd.setTypeOfDocumentId(typeOfDocument.getId());
        }
        else{
            documentToAdd.setTypeOfDocumentId(optionalTypeOfDocument.get().getId());
        }
        Boolean status = documentService.addDocument(documentToAdd);
        if(status){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new ErrorAnswer("SystemError"), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
