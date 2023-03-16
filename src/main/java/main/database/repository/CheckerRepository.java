package main.database.repository;

import main.database.entity.Checker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckerRepository extends JpaRepository<Checker, Long> {
    Checker getCheckerById(Long id);
}
