package main.database.repository;

import main.database.entity.Bookkeeping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookkeepingRepository extends JpaRepository<Bookkeeping, Long> {
    Bookkeeping getBookkeepingById(Long id);
}
