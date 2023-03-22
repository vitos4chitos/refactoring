package main.controller;

import main.database.service.SignatureAgregatorService;
import main.entity.responce.BaseAnswer;
import main.entity.request.SignatureRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signatures")
public class SignaturesController {

    @Autowired
    SignatureAgregatorService signaturesService;

    @PostMapping()
    ResponseEntity<BaseAnswer> makeSign(@RequestBody SignatureRequest sign) {
        return signaturesService.makeSign(sign);
    }
}

