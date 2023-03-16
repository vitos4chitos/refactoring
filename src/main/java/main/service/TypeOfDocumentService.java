package main.service;

import main.entity.Document;
import main.entity.Privileges;
import main.entity.TypeOfDocument;
import main.entity.User;
import main.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TypeOfDocumentService {
    private final DocumentRepository documentRepository;
    private final StatusRepository statusRepository;
    private final TypeOfDocumentRepository typeOfDocumentRepository;
    private final SignatureRepository signatureRepository;
    private final ProductionRepository productionRepository;
    private final PrivilegesRepository privilegesRepository;
    private final ParameterRepository parameterRepository;
    private final BookkeepingRepository bookkeepingRepository;
    private final UserRepository userRepository;

    public TypeOfDocumentService(DocumentRepository documentRepository, StatusRepository statusRepository, TypeOfDocumentRepository typeOfDocument, SignatureRepository signatureRepository, ProductionRepository productionRepository, PrivilegesRepository privilegesRepository, ParameterRepository parameterRepository, BookkeepingRepository bookkeepingRepository, UserRepository userRepository) {
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

    public List<TypeOfDocument> getTypes(long id){
        return typeOfDocumentRepository.getTypeOfDocumentsByInstanceId(id);
    }

    public String addTypeOfDocument(TypeOfDocument typeOfDocument) {
        try {
            typeOfDocumentRepository.save(typeOfDocument);
            return "{\"token\": \"true\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"token\": \"err\"}";
        }
    }
    public TypeOfDocument getById(long id){
        return typeOfDocumentRepository.getTypeOfDocumentById(id);
    }

    public TypeOfDocument getByName(String name){
        return typeOfDocumentRepository.getTypeOfDocumentByName(name);
    }
}
