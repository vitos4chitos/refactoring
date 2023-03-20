package main.database.repository;

import main.database.entity.Checker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckerRepository extends JpaRepository<Checker, Long> {
    Optional<Checker> getCheckerById(Long id);
}
