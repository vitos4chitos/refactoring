package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.database.entity.*;
import main.database.repository.SignatureRepository;
import main.entity.Sign;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignaturesService {

    private final SignatureRepository signaturesRepository;

    public List<Signature> getSignsById(Long id) {
        log.info("Получил подписи с id = {}", id);
        return signaturesRepository.getSignaturesByParametersId(id);
    }

    public void save(Long signId) {
        Signature signature = signaturesRepository.getSignatureById(signId);
        log.info("Обновляется подпись в {}", signature);
        signature.setIsSubscribed(true);
        signaturesRepository.save(signature);
        log.info("Проверяется валидность данных для parameters_id = {}", signature.getParametersId());
        signaturesRepository.verificationOfSignatures(signature.getParametersId());
    }

    public void save(Signature signature) {
        signaturesRepository.save(signature);
    }

    public List<Signature> getSignsByOffId(Long id) {
        return signaturesRepository.getSignatureByOfficialId(id);
    }

    private Signature getSignatureById(Long id) {
        return signaturesRepository.getSignatureById(id);
    }

    public ResponseEntity<Signature> getSignature(Long id){
        log.info("Поступил запрос на получение подписи документа id = {}", id);
        if(signaturesRepository.existsById(id)){
            log.info("Подпись найдена");
            return new ResponseEntity<>(getSignatureById(id), HttpStatus.OK);
        }
        log.error("Подпись не найдена");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
