package main.database.repository;

import main.database.entity.Prosecutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProsecutorRepository extends JpaRepository<Prosecutor, Long> {
    Prosecutor getProsecutorById(Long id);
}
