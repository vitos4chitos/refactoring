package main.repository;

import main.entity.Official;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfficialRepository extends JpaRepository<Official, Long> {
    Official getOfficialById(Long id);
    Official getOfficialByLogin(String login);
    List<Official> getOfficialByInstanceId(Long instanceId);
}
