package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import main.database.entity.Official;
import main.database.repository.OfficialRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfficialService {

    private final OfficialRepository officialRepository;
    public Official getOfficialById(long id){
        return officialRepository.getOfficialById(id);
    }

    public Official getOfficialByLogin(String login){
        return officialRepository.getOfficialByLogin(login);
    }

    public List<Official> getOfficialByInstanceId(Long id) {
         return officialRepository.getOfficialByInstanceId(id);
    }
}
