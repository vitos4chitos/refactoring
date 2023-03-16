package main.service;

import main.entity.*;
import main.repository.*;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

@Service
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

    public DocumentService(DocumentRepository documentRepository, StatusRepository statusRepository, TypeOfDocumentRepository typeOfDocument, SignatureRepository signatureRepository, ProductionRepository productionRepository, PrivilegesRepository privilegesRepository, ParameterRepository parameterRepository, BookkeepingRepository bookkeepingRepository, UserRepository userRepository) {
        this.documentRepository = documentRepository;
        this.statusRepository = statusRepository;
        this.typeOfDocumentRepository = typeOfDocument;
        this.signatureRepository = signatureRepository;
        this.productionRepository = productionRepository;
        this.privilegesRepository = privilegesRepository;
        this.parameterRepository = parameterRepository;
        this.bookkeepingRepository = bookkeepingRepository;
        this.userRepository = userRepository;
    }

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

    public void addForReview(Long parameterId) {
        documentRepository.addForReview(parameterId);
    }
}
