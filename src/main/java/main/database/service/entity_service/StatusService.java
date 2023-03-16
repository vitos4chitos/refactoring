package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import main.database.entity.Status;
import main.database.repository.StatusRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatusService {

    private StatusRepository statusRepository;
    public Status getStatusByParamId(Long id){
        return statusRepository.getStatusByParameterId(id);
    }

    public Status getStatusById(Long id) {
        return statusRepository.getStatusById(id);
    }
}
