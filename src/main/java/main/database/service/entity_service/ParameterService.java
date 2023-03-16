package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import main.database.entity.Parameter;
import main.database.repository.ParameterRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParameterService {
    private ParameterRepository parameterRepository;
    public Parameter getByParameterId(Long id){
        return parameterRepository.getParameterById(id);
    }

    public void save(Parameter parameter){
        parameterRepository.save(parameter);
    }
}
