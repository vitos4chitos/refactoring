package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.database.entity.Privileges;
import main.database.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrivilegesService {
    private final PrivilegesRepository privilegesRepository;

    public String addPrivileges(Privileges privileges) {
        try {
            privilegesRepository.save(privileges);
            return "{\"token\": \"true\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"token\": \"err\"}";
        }
    }

    public List<Privileges> getAll(int sale) {
        return privilegesRepository.getPrivilegesBySale(sale);
    }

    public Privileges getPrivilegesById(Long id) {
        return privilegesRepository.getPrivilegesById(id);
    }

    public ResponseEntity<Privileges> getPrivilege(Long id){
        log.info("Поступил запрос на получение привелегии id = {}", id);
        if(privilegesRepository.existsById(id)){
            log.info("Привелегии найдены");
            return new ResponseEntity<>(getPrivilegesById(id), HttpStatus.OK);
        }
        log.error("Привелегии не найдены");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
