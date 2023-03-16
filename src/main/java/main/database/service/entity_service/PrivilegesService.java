package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import main.database.entity.Privileges;
import main.database.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
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

    public List<Privileges> getAll(int sale){
        return privilegesRepository.getPrivilegesBySale(sale);
    }

    public Privileges getPrivilegesById(Long id){
        return privilegesRepository.getPrivilegesById(id);
    }
}
