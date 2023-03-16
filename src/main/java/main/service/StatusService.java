package main.service;

import main.entity.Status;
import main.repository.StatusRepository;
import org.springframework.stereotype.Service;

@Service
public class StatusService {

    private StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository){
        this.statusRepository = statusRepository;
    }

    public Status getStatusByParamId(Long id){
        return statusRepository.getStatusByParameterId(id);
    }
}
