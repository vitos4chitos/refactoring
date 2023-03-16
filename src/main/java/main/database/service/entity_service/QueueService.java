package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import main.database.entity.*;
import main.database.repository.*;
import main.entity.BackQueue;
import main.entity.FirstUser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QueueService {


    private final QueueRepository queueRepository;
    private final OfficialRepository officialRepository;
    private final UserRepository userRepository;

    public Queue getQueueById(Long id) {
        return queueRepository.getQueueById(id);
    }

    public List<Queue> getAllQueueByUserId(Long id) {
        List<Queue> queues = queueRepository.getQueuesByUserId(id);
        if (queues != null) {
            return queues;
        }
        return new ArrayList<>();
    }
    public List<Queue> getQueueByOfficialUsername(String login) {
        Official official = officialRepository.getOfficialByLogin(login);
        if (official != null) {
            List<Queue> queue = queueRepository.getQueueByOfficialId(official.getId());
            if (queue != null)
                return queue;
        }
        return new ArrayList<>();
    }

    public User getFirstUserFromQueueByOfficialUsername(String login) {
        List<Queue> queue = this.getQueueByOfficialUsername(login).stream().filter(e -> e.getPlace() == 1).collect(Collectors.toList());
        Long firstUserId = queue.get(0).getUserId();
        User user = userRepository.getUserById(firstUserId);
        if (user != null)
            return user;
        return new User();
    }

    public Boolean advanceQueue(Long officialId) {
        return queueRepository.advanceQueue(officialId);
    }

    public void putInQueue(Long userId, Long officialId) {
        queueRepository.putInQueue(userId, officialId);
    }
}
