package main.database.service;

import lombok.RequiredArgsConstructor;
import main.database.entity.Document;
import main.database.entity.Official;
import main.database.entity.Parameter;
import main.database.entity.Signature;
import main.database.service.entity_service.*;
import main.entity.Sign;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SignatureAgregatorService {
    private final DocumentService documentService;
    private final ParameterService parameterService;
    private final OfficialService officialService;
    private final QueueService queueService;
    private final SignaturesService signaturesService;

    public Boolean makeSign(Sign sign){
        System.out.println(sign.getLoginOff());
        Long idSign;
        Official official = officialService.getOfficialByLogin(sign.getLoginOff());
        List<Document> documentList = documentService.getAllDocumentsByUserId(sign.getUserId());
        List<Parameter> parameters = new ArrayList<>();
        Parameter parameter;
        for (Document document : documentList) {
            if (document.getParameters_id() != null) {
                parameter = parameterService.getByParameterId(document.getParameters_id());
                parameters.add(parameter);
            }
        }
        List<Signature> signatures;
        boolean flag = false;
        for (Parameter value : parameters) {
            signatures = signaturesService.getSignsById(value.getId());
            for (Signature signature : signatures) {
                if (signature.getOfficialId().equals(official.getId()) && !signature.getIsSubscribed()) {
                    idSign = signature.getId();
                    signaturesService.save(idSign);
                    flag = true;
                }
                if(flag) break;
            }
            if(flag) break;
        }
        return queueService.advanceQueue(official.getId());
    }
}
