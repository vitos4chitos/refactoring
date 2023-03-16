package main.database.service;

import main.database.entity.Official;
import main.database.repository.OfficialRepository;
import org.springframework.stereotype.Service;

@Service
public class OfficialService {

    private final OfficialRepository officialRepository;

    public OfficialService(OfficialRepository officialRepository) {
        this.officialRepository = officialRepository;
    }

    public Official getOfficialById(long id){
        return officialRepository.getOfficialById(id);
    }

    public Official getOfficialByLogin(String login){
        return officialRepository.getOfficialByLogin(login);
    }
}
