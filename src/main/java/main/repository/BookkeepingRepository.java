package main.repository;

import main.entity.Bookkeeping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookkeepingRepository extends JpaRepository<Bookkeeping, Long> {
    Bookkeeping getBookkeepingById(Long id);
}
