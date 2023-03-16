package main.controller;

import lombok.Data;
import lombok.NoArgsConstructor;
import main.entity.*;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/signatures")
public class SignaturesController {

    @Autowired
    UserService userService;
    @Autowired
    OfficialService officialService;
    @Autowired
    DocumentService documentService;
    @Autowired
    ParameterService parameterService;
    @Autowired
    SignaturesService signaturesService;
    @Autowired
    QueueService queueService;

    @PostMapping("/makeSign")
    Boolean makeSign(@RequestBody Sign sign){
        System.out.println(sign.loginOff);
        Long idSign;
        Official official = officialService.getOfficialByLogin(sign.loginOff);
        List<Document> documentList = documentService.getAllDocumentsByUserId(sign.userId);
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

    @Data
    @NoArgsConstructor
    public static class Sign {
        Long userId;
        String loginOff;
    }
}

