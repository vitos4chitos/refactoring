package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.database.entity.*;
import main.database.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueService {


    private final QueueRepository queueRepository;

    private Optional<Queue> getQueueById(Long id) {
        return queueRepository.getQueueById(id);
    }

    public ResponseEntity<Queue> getQueue(Long id){
        log.info("Поступил запрос на поиск очереди id = {}", id);
        Optional<Queue> optionalQueue = getQueueById(id);
        if(optionalQueue.isPresent()){
            log.info("Очередь найдена: {}", optionalQueue.get());
            return new ResponseEntity<>(optionalQueue.get(), HttpStatus.OK);
        }
        log.error("Очередь не найдена");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    public ResponseEntity<List<Queue>> getAllQueueByUserId(Long id) {
        log.info("Поступил запрос на поиск очереди user_id = {}", id);
        List<Queue> queues = queueRepository.getQueuesByUserId(id);
        if (!queues.isEmpty()) {
            log.info("Очереди найдены: {}", queues);
            return new ResponseEntity<>(queues, HttpStatus.OK);
        }
        log.info("Пользователь не стоит в очередях");
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    public List<Queue> getQueueByOfficialUsername(Official official) {
        log.info("Поступил запрос на поиск очереди official_login = {}", official.getLogin());
        List<Queue> queue = queueRepository.getQueueByOfficialId(official.getId());
        if (!queue.isEmpty()) {
            log.info("Очередь найдена: {}", queue);
            return queue;
        }
        log.info("Очередь пуста");
        return queue;
    }

    public Long getFirstUserIdFromQueueByOfficialUsername(Official official) {
        List<Queue> queue = this.getQueueByOfficialUsername(official).stream().filter(e -> e.getPlace() == 1).collect(Collectors.toList());
        if(!queue.isEmpty()){
            return queue.get(0).getUserId();
        }
        return -1L;


    }

    public Boolean advanceQueue(Long officialId) {
        log.info("Продвигается очередь дальше у оф. лица id = {}", officialId);
        return queueRepository.advanceQueue(officialId);
    }

    public void putInQueue(Long userId, Long officialId) {
        log.info("Добавляю пользователя id = {} в очередь officailId = {}", userId, officialId);
        queueRepository.putInQueue(userId, officialId);
    }

    public void putInQueue(User user, List<Official> officials){
        officials.forEach(of -> putInQueue(user.getId(), of.getId()));
    }
}
