package main.database.service;

import lombok.RequiredArgsConstructor;
import main.controller.SignaturesController;
import main.database.entity.Document;
import main.database.entity.Official;
import main.database.entity.Parameter;
import main.database.entity.Signature;
import main.database.repository.SignatureRepository;
import main.entity.Sign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SignaturesService {

    private final SignatureRepository signaturesRepository;
    private final OfficialService officialService;
    private final DocumentService documentService;
    private final ParameterService parameterService;
    private final QueueService queueService;

    public List<Signature> getSignsById(Long id){
        return signaturesRepository.getSignaturesByParametersId(id);
    }

    public void save(Long signId) {
        Signature signature = signaturesRepository.getSignatureById(signId);
        signature.setIsSubscribed(true);
        signaturesRepository.save(signature);
        signaturesRepository.verificationOfSignatures(signature.getParametersId());
    }

    public  List<Signature> getSignsByOffId(Long id){
        return signaturesRepository.getSignatureByOfficialId(id);
    }

    public Boolean makeSign(Sign sign){
        System.out.println(sign.getLoginOff());
        Long idSign;
        Official official = officialService.getOfficialByLogin(sign.getLoginOff());
        List<Document> documentList = documentService.getAllDocumentsByUserId(sign.getUserId());
        List<Parameter> parameters = new ArrayList<>();
        Parameter parameter;
        for (Document document : documentList) {
            if (document.getParameters_id() != null) {
                parameter = parameterService.getByParametrId(document.getParameters_id());
                parameters.add(parameter);
            }
        }
        List<Signature> signatures;
        boolean flag = false;
        for (Parameter value : parameters) {
            signatures = getSignsById(value.getId());
            for (Signature signature : signatures) {
                if (signature.getOfficialId().equals(official.getId()) && !signature.getIsSubscribed()) {
                    idSign = signature.getId();
                    save(idSign);
                    flag = true;
                }
                if(flag) break;
            }
            if(flag) break;
        }
        return queueService.advanceQueue(official.getId());
    }
}
