package main.database.repository;

import main.database.entity.Bookkeeping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookkeepingRepository extends JpaRepository<Bookkeeping, Long> {
    Optional<Bookkeeping> getBookkeepingById(Long id);
}
