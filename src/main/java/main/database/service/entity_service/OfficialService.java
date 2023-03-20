package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.database.entity.Official;
import main.database.repository.OfficialRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfficialService {

    private final OfficialRepository officialRepository;

    public Official getOfficialById(long id) {
        return officialRepository.getOfficialById(id);
    }

    public ResponseEntity<Official> getOfficial(long id){
        log.info("Поступил запрос на получение оф.лица id = {}", id);
        if(officialRepository.existsById(id)){
            log.info("Оф.лицо найдено");
            return new ResponseEntity<>(getOfficialById(id), HttpStatus.OK);
        }
        log.error("Оф.лицо не найдено");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    public Official getOfficialByLogin(String login) {
        Optional<Official> officialOptional = officialRepository.getOfficialByLogin(login);
        if(officialOptional.isPresent()){
            log.info("Должностное лицо с логином {} было найдено", login);
            return officialOptional.get();
        }
        log.error("Должностное лицо с логином {} не было найдено", login);
        return null;
    }

    public List<Official> getOfficialByInstanceId(Long id) {
        return officialRepository.getOfficialByInstanceId(id);
    }
}
