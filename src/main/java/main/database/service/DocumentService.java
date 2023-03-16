package main.database.service;

import lombok.RequiredArgsConstructor;
import main.database.entity.*;
import main.database.repository.*;
import main.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final StatusRepository statusRepository;
    private final TypeOfDocumentRepository typeOfDocumentRepository;
    private final SignatureRepository signatureRepository;
    private final ProductionRepository productionRepository;
    private final PrivilegesRepository privilegesRepository;
    private final ParameterRepository parameterRepository;
    private final BookkeepingRepository bookkeepingRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final TypeOfDocumentService typeOfDocumentService;
    private final PrivilegesService privilegesService;
    private final SignaturesService signaturesService;
    private final OfficialService officialService;
    private final StatusService statusService;
    private final ParameterService parameterService;

    public Document getDocumentById(Long id) {
        return documentRepository.getDocumentById(id);
    }
    public Status getStatusById(Long id) {
        return statusRepository.getStatusById(id);
    }

    public TypeOfDocument getTypeOfDocumentId(Long id) {
        return typeOfDocumentRepository.getTypeOfDocumentById(id);
    }

    public Signature getSignatureById(Long id) {
        return signatureRepository.getSignatureById(id);
    }

    public Production getProductionById(Long id) {
        return productionRepository.getProductionById(id);
    }

    public Privileges getPrivilegesById(Long id) {
        return privilegesRepository.getPrivilegesById(id);
    }

    public Parameter getParameterById(Long id) {
        return parameterRepository.getParameterById(id);
    }

    public Bookkeeping getBookkeepingById(Long id) {
        return bookkeepingRepository.getBookkeepingById(id);
    }

    public List<Document> getAllDocumentsByUserId(Long userId) {
        List<Document> documentList = documentRepository.getDocumentsByUserId(userId);
        if (documentList != null) {
            return documentList;
        }
        return new ArrayList<>();
    }

    public List<TypeOfDocument> getAllTypeOfDocumentByInstanceId(Long id) {
        List<TypeOfDocument> typeOfDocuments = typeOfDocumentRepository.getTypeOfDocumentsByInstanceId(id);
        if (typeOfDocuments != null) {
            return typeOfDocuments;
        }
        return new ArrayList<>();
    }

    public List<TypeOfDocument> getNameTypeOfDocumentsWhichNotExistInDocuments(Long userId) {
        User user = userRepository.getUserById(userId);
        Long instanceId = user.getInstanceId();
        List<TypeOfDocument> typeOfDocuments = this.getAllTypeOfDocumentByInstanceId(instanceId);
        List<TypeOfDocument> ans = new ArrayList<>();
        for (TypeOfDocument td : typeOfDocuments) {
            boolean flag = documentRepository.existsDocumentByTypeOfDocumentIdAndUserId(td.getId(), userId);
            System.out.println(flag);
            if (!flag) {
                ans.add(td);
            }
        }
        return ans;
    }

    public String addDocument(Document document) {
        try {
            documentRepository.save(document);
            return "{\"token\": \"true\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"token\": \"err\"}";
        }
    }

    public String addForReview(Long parameterId) {
        documentRepository.addForReview(parameterId);
        return "{\"token\": \"true\"}";
    }

    public List<BackVals> getUsersDocuments(@RequestParam("login") String id) {
        long idd = userService.getUserId(id);
        List<Document> documents = getAllDocumentsByUserId(idd);
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
        Document document = getDocumentById(id);
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
        return addDocument(document1);
    }

    public List<Reference> getAllReference(String login){
        System.out.println(login);
        long idd = userService.getUserId(login);
        List<Reference> references = new ArrayList<>();
        List<Document> documents = getAllDocumentsByUserId(idd);
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
        return addDocument(document1);
    }
}
