package main.database.repository;

import main.database.entity.Prosecutor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProsecutorRepository extends JpaRepository<Prosecutor, Long> {
    Optional<Prosecutor> getProsecutorById(Long id);
}
