package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.database.entity.Status;
import main.database.entity.TypeOfDocument;
import main.database.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TypeOfDocumentService {
    private final TypeOfDocumentRepository typeOfDocumentRepository;

    public List<TypeOfDocument> getTypes(long id) {
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

    public TypeOfDocument getById(long id) {
        return typeOfDocumentRepository.getTypeOfDocumentById(id);
    }

    public ResponseEntity<TypeOfDocument> getTypeOfDocument(Long id){
        log.info("Поступил запрос на получение типа документа id = {}", id);
        if(typeOfDocumentRepository.existsById(id)){
            log.info("Тип найден");
            return new ResponseEntity<>(getById(id), HttpStatus.OK);
        }
        log.error("Тип не найден");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public TypeOfDocument getByName(String name) {
        return typeOfDocumentRepository.getTypeOfDocumentByName(name);
    }

    public List<TypeOfDocument> getAllTypeOfDocumentByInstanceId(Long id) {
        List<TypeOfDocument> typeOfDocuments = typeOfDocumentRepository.getTypeOfDocumentsByInstanceId(id);
        if (typeOfDocuments != null) {
            return typeOfDocuments;
        }
        return new ArrayList<>();
    }

    public boolean isPresentByName(String name){
        return typeOfDocumentRepository.existsByName(name);
    }

}
