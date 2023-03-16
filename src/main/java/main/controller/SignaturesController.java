package main.controller;

import lombok.Data;
import lombok.NoArgsConstructor;
import main.database.entity.Document;
import main.database.entity.Official;
import main.database.entity.Parameter;
import main.database.entity.Signature;
import main.database.service.*;
import main.entity.Sign;
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
    SignaturesService signaturesService;

    @PostMapping()
    Boolean makeSign(@RequestBody Sign sign) {
        return signaturesService.makeSign(sign);
    }
}

