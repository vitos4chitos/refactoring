package main.database.repository;

import main.database.entity.Official;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OfficialRepository extends JpaRepository<Official, Long> {
    Official getOfficialById(Long id);

    Optional<Official> getOfficialByLogin(String login);

    List<Official> getOfficialByInstanceId(Long instanceId);

    boolean existsById(@NotNull Long id);
}
