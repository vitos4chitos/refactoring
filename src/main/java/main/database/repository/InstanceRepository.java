package main.database.repository;

import main.database.entity.Instance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface InstanceRepository extends JpaRepository<Instance, Long> {

    Optional<Instance> getInstanceById(Long id);

    @Transactional
    @Procedure(procedureName = "transfer_to_the_next_level")
    boolean transferToTheNextLevel(Long userId);
}
