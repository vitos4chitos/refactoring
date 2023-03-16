package main.repository;

import main.entity.Instance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface InstanceRepository extends JpaRepository<Instance, Long> {

    Instance getInstanceById(Long id);

    @Transactional
    @Procedure(procedureName = "transfer_to_the_next_level")
    boolean transferToTheNextLevel(@Param("user_id") Long userId);
}
