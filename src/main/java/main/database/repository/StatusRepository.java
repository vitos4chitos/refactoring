package main.database.repository;

import main.database.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {
    Status getStatusById(Long id);
    Status getStatusByParameterId(Long id);
}
