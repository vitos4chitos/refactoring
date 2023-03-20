package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.database.entity.Status;
import main.database.repository.StatusRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatusService {

    private final StatusRepository statusRepository;

    public Status getStatusByParamId(Long id) {
        return statusRepository.getStatusByParameterId(id);
    }

    private Status getStatusById(Long id) {
        return statusRepository.getStatusById(id);
    }

    public ResponseEntity<Status> getStatus(Long id){
        log.info("Поступил запрос на получение статуса документа id = {}", id);
        if(statusRepository.existsById(id)){
            log.info("Статус найден");
            return new ResponseEntity<>(getStatusById(id), HttpStatus.OK);
        }
        log.error("Статус не найден");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
