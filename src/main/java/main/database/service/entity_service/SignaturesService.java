package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import main.database.entity.Document;
import main.database.entity.Official;
import main.database.entity.Parameter;
import main.database.entity.Signature;
import main.database.repository.SignatureRepository;
import main.entity.Sign;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SignaturesService {

    private final SignatureRepository signaturesRepository;

    public List<Signature> getSignsById(Long id){
        return signaturesRepository.getSignaturesByParametersId(id);
    }

    public void save(Long signId) {
        Signature signature = signaturesRepository.getSignatureById(signId);
        signature.setIsSubscribed(true);
        signaturesRepository.save(signature);
        signaturesRepository.verificationOfSignatures(signature.getParametersId());
    }

    public void save(Signature signature) {
        signaturesRepository.save(signature);
    }

    public  List<Signature> getSignsByOffId(Long id){
        return signaturesRepository.getSignatureByOfficialId(id);
    }

    public Signature getSignatureById(Long id) {
        return signaturesRepository.getSignatureById(id);
    }
}
