package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.database.entity.*;
import main.database.repository.*;
import main.entity.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

    private final DocumentRepository documentRepository;

    public Document getDocumentById(Long id) {
        return documentRepository.getDocumentById(id);
    }

    public ResponseEntity<Document> getDocument(Long id){
        log.info("Поступил запрос на получение документа id = {}", id);
        if(documentRepository.existsById(id)){
            log.info("Документ найден");
            return new ResponseEntity<>(getDocumentById(id), HttpStatus.OK);
        }
        log.error("Документ не найден");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public List<Document> getAllDocumentsByUserId(Long userId) {
        List<Document> documentList = documentRepository.getDocumentsByUserId(userId);
        if (documentList != null) {
            log.info("Были найдены следующие документы польщователя id = {}: {}", userId, documentList);
            return documentList;
        }
        log.error("Не найдены документы польщователя id = {}", userId);
        return new ArrayList<>();
    }

    public Boolean addDocument(Document document) {
        try {
            documentRepository.save(document);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public ResponseEntity<BaseAnswer> addForReview(Long parameterId) {
        log.info("Поступил запрос на добавление на проверку параметра id = {}", parameterId);
        if(isExistById(parameterId)) {
            try {
                documentRepository.addForReview(parameterId);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                log.error(e.getMessage());
                return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
            }
        }
        return new ResponseEntity<>(new ErrorAnswer("ParameterNotFound"), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private boolean isExistById(Long parameterId){
        return documentRepository.existsById(parameterId);
    }

    public Long calculateSale(Long UserId) {
        return documentRepository.calculateSale(UserId);
    }

    public void save(Document document) {
        documentRepository.save(document);
    }

    public boolean existsDocumentByTypeOfDocumentIdAndUserId(Long typeOfDocumentId, Long userId) {
        return documentRepository.existsDocumentByTypeOfDocumentIdAndUserId(typeOfDocumentId, userId);
    }
}
