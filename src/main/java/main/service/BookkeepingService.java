package main.service;

import main.entity.*;
import main.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

@Service
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

    public BookkeepingService(BookkeepingRepository bookkeepingRepository, UserRepository userRepository, ProductionRepository productionRepository, TypeOfDocumentRepository typeOfDocumentRepository, OfficialRepository officialRepository, ParameterRepository parameterRepository, SignatureRepository signatureRepository, DocumentRepository documentRepository, QueueRepository queueRepository) {
        this.bookkeepingRepository = bookkeepingRepository;
        this.userRepository = userRepository;
        this.productionRepository = productionRepository;
        this.typeOfDocumentRepository = typeOfDocumentRepository;
        this.officialRepository = officialRepository;
        this.parameterRepository = parameterRepository;
        this.signatureRepository = signatureRepository;
        this.documentRepository = documentRepository;
        this.queueRepository = queueRepository;
    }

    // купить справку (логин), id bookkeeping, название справки
    // назначить трех чуваков

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
}
