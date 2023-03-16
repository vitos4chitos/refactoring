package main.repository;

import main.entity.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface QueueRepository extends JpaRepository<Queue, Long> {
    Queue getQueueById(Long id);
    List<Queue> getQueuesByUserId(Long userId);
    List<Queue> getQueueByOfficialId(Long officialId);

    @Transactional
    @Procedure(procedureName = "advance_the_queue")
    boolean advanceQueue(@Param("official_id") Long officialId);

    @Transactional
    @Procedure(procedureName = "put_in_queue")
    Long putInQueue(@Param("user_id") Long userId, @Param("official_id") Long officialId);

}
