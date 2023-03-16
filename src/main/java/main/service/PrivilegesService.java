package main.service;

import main.entity.Privileges;
import main.entity.User;
import main.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrivilegesService {

    private final DocumentRepository documentRepository;
    private final StatusRepository statusRepository;
    private final TypeOfDocumentRepository typeOfDocumentRepository;
    private final SignatureRepository signatureRepository;
    private final ProductionRepository productionRepository;
    private final PrivilegesRepository privilegesRepository;
    private final ParameterRepository parameterRepository;
    private final BookkeepingRepository bookkeepingRepository;
    private final UserRepository userRepository;

    public PrivilegesService(DocumentRepository documentRepository, StatusRepository statusRepository, TypeOfDocumentRepository typeOfDocument, SignatureRepository signatureRepository, ProductionRepository productionRepository, PrivilegesRepository privilegesRepository, ParameterRepository parameterRepository, BookkeepingRepository bookkeepingRepository, UserRepository userRepository) {
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

    public String addPrivileges(Privileges privileges) {
        try {
            privilegesRepository.save(privileges);
            return "{\"token\": \"true\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"token\": \"err\"}";
        }
    }

    public List<Privileges> getAll(int sale){
        return privilegesRepository.getPrivilegesBySale(sale);
    }

    public Privileges getPrivilegesById(Long id){
        return privilegesRepository.getPrivilegesById(id);
    }
}
