package main.service;

import main.entity.Signature;
import main.repository.DocumentRepository;
import main.repository.SignatureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SignaturesService {

    private final SignatureRepository signaturesRepository;

    public SignaturesService(SignatureRepository signatureRepository){
        this.signaturesRepository = signatureRepository;
    }

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
}
