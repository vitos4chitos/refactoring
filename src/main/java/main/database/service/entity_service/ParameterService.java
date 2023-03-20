package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.database.entity.Parameter;
import main.database.repository.ParameterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParameterService {
    private final ParameterRepository parameterRepository;

    public Parameter getByParameterId(Long id) {
        log.info("Найден параметр id = {}", id);
        return parameterRepository.getParameterById(id);
    }

    public ResponseEntity<Parameter> getParameter(Long id){
        log.info("Поступил запрос на получение параметра id = {}", id);
        if(parameterRepository.existsById(id)){
            log.info("Параметр найден");
            return new ResponseEntity<>(getByParameterId(id), HttpStatus.OK);
        }
        log.error("Параметр не найден");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    public void save(Parameter parameter) {
        parameterRepository.save(parameter);
    }
}
