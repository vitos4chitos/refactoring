package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import main.database.entity.*;
import main.database.repository.*;
import main.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;

    public Document getDocumentById(Long id) {
        return documentRepository.getDocumentById(id);
    }

    public List<Document> getAllDocumentsByUserId(Long userId) {
        List<Document> documentList = documentRepository.getDocumentsByUserId(userId);
        if (documentList != null) {
            return documentList;
        }
        return new ArrayList<>();
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

    public Long calculateSale(Long UserId) {
        return documentRepository.calculateSale(UserId);
    }

    public void save(Document document){
        documentRepository.save(document);
    }

    public boolean existsDocumentByTypeOfDocumentIdAndUserId(Long typeOfDocumentId, Long userId) {
        return documentRepository.existsDocumentByTypeOfDocumentIdAndUserId(typeOfDocumentId, userId);
    }
}
