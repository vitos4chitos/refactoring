package main.database.service;

import main.database.entity.Status;
import main.database.repository.StatusRepository;
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
