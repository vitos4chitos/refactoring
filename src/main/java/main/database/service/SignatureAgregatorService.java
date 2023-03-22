package main.database.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.database.entity.Document;
import main.database.entity.Official;
import main.database.entity.Parameter;
import main.database.entity.Signature;
import main.database.service.entity_service.*;
import main.entity.responce.BaseAnswer;
import main.entity.responce.ErrorAnswer;
import main.entity.request.SignatureRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignatureAgregatorService {
    private final DocumentService documentService;
    private final ParameterService parameterService;
    private final OfficialService officialService;
    private final QueueService queueService;
    private final SignaturesService signaturesService;

    public ResponseEntity<BaseAnswer> makeSign(SignatureRequest sign) {
        log.info("Поступил запрос на подпись: {}", sign);
        Official official = officialService.getOfficialByLogin(sign.getOfficialLogin());
        if(official == null){
            return new ResponseEntity<>(
                    ErrorAnswer
                    .builder()
                    .message("OfficialNotFound").build(),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
        log.info("Делаю выборку документов");
        List<Document> documentList = documentService.getAllDocumentsByUserId(sign.getUserId());
        log.info("Делаю выборку параметров");
        List<Parameter> parameters = new ArrayList<>();
        documentList.stream()
                .filter(d -> d.getParametersId() != null)
                .forEach(d -> parameters.add(parameterService.getByParameterId(d.getParametersId())));
        log.info("Ставлю подпись");
        for (Parameter value : parameters) {
            List<Signature> signatures = signaturesService.getSignsById(value.getId());
            Optional<Signature> optional =signatures.stream()
                    .filter(signature -> signature.getOfficialId().equals(official.getId()) && !signature.getIsSubscribed())
                    .findFirst();
            if(optional.isPresent()){
                signaturesService.save(optional.get().getId());
                break;
            }
        }
        queueService.advanceQueue(official.getId());
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
