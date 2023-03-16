package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import main.database.entity.TypeOfDocument;
import main.database.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeOfDocumentService {
    private final TypeOfDocumentRepository typeOfDocumentRepository;

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

    public List<TypeOfDocument> getAllTypeOfDocumentByInstanceId(Long id) {
        List<TypeOfDocument> typeOfDocuments = typeOfDocumentRepository.getTypeOfDocumentsByInstanceId(id);
        if (typeOfDocuments != null) {
            return typeOfDocuments;
        }
        return new ArrayList<>();
    }

}
