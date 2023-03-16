package main.service;

import main.entity.*;
import main.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class QueueService {


    private final QueueRepository queueRepository;
    private final ScheduleRepository scheduleRepository;
    private final OfficialRepository officialRepository;
    private final UserRepository userRepository;
    private final SignatureRepository signatureRepository;
    private final DocumentRepository documentRepository;
    private final ParameterRepository parameterRepository;

    public QueueService(QueueRepository queueRepository, ScheduleRepository scheduleRepository, OfficialRepository officialRepository, UserRepository userRepository, SignatureRepository signatureRepository, DocumentRepository documentRepository, ParameterRepository parameterRepository) {
        this.queueRepository = queueRepository;
        this.scheduleRepository = scheduleRepository;
        this.officialRepository = officialRepository;
        this.userRepository = userRepository;
        this.signatureRepository = signatureRepository;
        this.documentRepository = documentRepository;
        this.parameterRepository = parameterRepository;
    }

    public Queue getQueueById(Long id) {
        return queueRepository.getQueueById(id);
    }

    public Schedule getScheduleById(Long id) {
        return scheduleRepository.getScheduleById(id);
    }

    public List<Queue> getAllQueueByUserId(Long id) {
        List<Queue> queues = queueRepository.getQueuesByUserId(id);
        if (queues != null) {
            return queues;
        }
        return new ArrayList<>();
    }

    // получить очередь для чиновника по логину
    // получить первого из очереди для конкретного чиновника по логину

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
}
