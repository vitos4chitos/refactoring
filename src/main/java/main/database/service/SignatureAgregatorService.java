package main.database.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.database.entity.Document;
import main.database.entity.Official;
import main.database.entity.Parameter;
import main.database.entity.Signature;
import main.database.service.entity_service.*;
import main.entity.BaseAnswer;
import main.entity.ErrorAnswer;
import main.entity.Sign;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignatureAgregatorService {
    private final DocumentService documentService;
    private final ParameterService parameterService;
    private final OfficialService officialService;
    private final QueueService queueService;
    private final SignaturesService signaturesService;

    public ResponseEntity<BaseAnswer> makeSign(Sign sign) {
        log.info("Поступил запрос на подпись: {}", sign);
        Official official = officialService.getOfficialByLogin(sign.getLoginOff());
        if(official == null){
            return new ResponseEntity<>(ErrorAnswer
                    .builder()
                    .message("OfficialNotFound").build(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        log.info("Заполняю документы");
        List<Document> documentList = documentService.getAllDocumentsByUserId(sign.getUserId());
        log.info("Заполняю параметры");
        List<Parameter> parameters = new ArrayList<>();
        documentList.stream()
                .filter(d -> d.getParametersId() != null)
                .forEach(d -> parameters.add(parameterService.getByParameterId(d.getParametersId())));
        log.info("Заполняю подписи");
        List<Signature> signatures;
        boolean flag = false;
        for (Parameter value : parameters) {
            signatures = signaturesService.getSignsById(value.getId());
            for (Signature signature : signatures) {
                if (signature.getOfficialId().equals(official.getId()) && !signature.getIsSubscribed()) {
                    Long idSign = signature.getId();
                    signaturesService.save(idSign);
                    flag = true;
                }
                if (flag) break;
            }
            if (flag) break;
        }
        queueService.advanceQueue(official.getId());
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
