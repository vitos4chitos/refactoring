package main.controller;

import main.database.service.SignatureAgregatorService;
import main.database.service.entity_service.SignaturesService;
import main.entity.Sign;
import org.springframework.beans.factory.annotation.Autowired;
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
    Boolean makeSign(@RequestBody Sign sign) {
        return signaturesService.makeSign(sign);
    }
}

