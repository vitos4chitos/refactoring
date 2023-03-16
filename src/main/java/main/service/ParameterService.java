package main.service;

import main.entity.Parameter;
import main.repository.ParameterRepository;
import org.springframework.stereotype.Service;

@Service
public class ParameterService {

    private ParameterRepository parameterRepository;

    public ParameterService(ParameterRepository parameterRepository){
        this.parameterRepository = parameterRepository;
    }

    public Parameter getByParametrId(Long id){
        return parameterRepository.getParameterById(id);
    }
}
