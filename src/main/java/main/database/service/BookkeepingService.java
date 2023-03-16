package main.database.service;

import lombok.RequiredArgsConstructor;
import main.database.entity.*;
import main.database.repository.*;
import main.entity.BackVals;
import main.entity.Shop;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookkeepingService {

    private final BookkeepingRepository bookkeepingRepository;
    private final UserRepository userRepository;
    private final ProductionRepository productionRepository;
    private final TypeOfDocumentRepository typeOfDocumentRepository;
    private final OfficialRepository officialRepository;
    private final ParameterRepository parameterRepository;
    private final SignatureRepository signatureRepository;
    private final DocumentRepository documentRepository;
    private final QueueRepository queueRepository;

    private final UserService userService;
    private final DocumentService documentService;
    private final TypeOfDocumentService typeOfDocumentService;

    private final ProductionService productionService;

    public boolean buyDocument(String login, Long bookkeepingId, String name) {
        Bookkeeping bookkeeping = bookkeepingRepository.getBookkeepingById(bookkeepingId);
        BigDecimal userMoney, productionCost;
        if (userRepository.getUserByLogin(login).isPresent()) {
            User user = userRepository.getUserByLogin(login).get();
            userMoney = user.getMoney();
            TypeOfDocument typeOfDocument = typeOfDocumentRepository.getTypeOfDocumentByName(name);
            //System.out.println(bookkeepingId);
            //System.out.println(typeOfDocument.getId());
            //System.out.println(documentRepository.calculateSale(user.getId()));
            Production production = productionRepository.getProductionByBookkeepingIdAndAndTypeOfDocumentId(bookkeepingId, typeOfDocument.getId());
            System.out.println("До" + production.getCost());
            System.out.println("Скидка" + documentRepository.calculateSale(user.getId()));
            productionCost = BigDecimal.valueOf(production.getCost().doubleValue() * ((100 - documentRepository.calculateSale(user.getId())) / 100.0));
            System.out.println("После" + productionCost);
            if (userMoney.subtract(productionCost).doubleValue() >= 0) {
                Signature signatureOne = new Signature();
                Signature signatureTwo = new Signature();
                Signature signatureThree = new Signature();
                Parameter parameterOne = new Parameter();
                parameterOne.setStatus(false);

                user.setMoney(userMoney.subtract(productionCost));
                userRepository.save(user);

                parameterRepository.save(parameterOne);


                List<Official> officials = officialRepository.getOfficialByInstanceId(user.getInstanceId());
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

                    signatureRepository.save(signatureOne);
                    signatureRepository.save(signatureTwo);
                    signatureRepository.save(signatureThree);

                    production.setQuantity(production.getQuantity() - 1);

                    productionRepository.save(production);

                    Document document = new Document();
                    document.setTypeOfDocumentId(typeOfDocument.getId());
                    java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                    document.setDateOfIssue(date);
                    document.setUserId(user.getId());
                    document.setValidity(null);
                    document.setIssuedByWhom("Замок");
                    document.setParameters_id(parameterOne.getId());
                    documentRepository.save(document);

                    queueRepository.putInQueue(user.getId(), officials.get(0).getId());
                    queueRepository.putInQueue(user.getId(), officials.get(1).getId());
                    queueRepository.putInQueue(user.getId(), officials.get(2).getId());

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

    public List<Shop> getShopsByDocumentName(String name){
        System.out.println(name);
        TypeOfDocument typeOfDocument = typeOfDocumentService.getByName(name);
        Long id = typeOfDocument.getId();
        System.out.println(id);
        List<Production> productions = productionService.getProductionByType(id);
        Shop shop;
        List<Shop> shops = new ArrayList<>();
        for (Production production : productions) {
            shop = new Shop();
            shop.setCost(production.getCost());
            shop.setId(production.getBookkeepingId());
            shop.setQuantity(production.getQuantity());
            System.out.println(production.getId());
            String s = String.format("%d:%02d:%02d",production.getTime().getSeconds() / 3600, (production.getTime().getSeconds() % 3600)
                    / 60, (production.getTime().getSeconds() % 60));
            System.out.println(s);
            shop.setTime(s);
            shops.add(shop);
        }
        return shops;
    }

}
