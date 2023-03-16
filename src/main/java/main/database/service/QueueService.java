package main.database.service;

import lombok.RequiredArgsConstructor;
import main.database.entity.*;
import main.database.repository.*;
import main.entity.BackQueue;
import main.entity.FirstUser;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QueueService {


    private final QueueRepository queueRepository;
    private final ScheduleRepository scheduleRepository;
    private final OfficialRepository officialRepository;
    private final UserRepository userRepository;
    private final SignatureRepository signatureRepository;
    private final DocumentRepository documentRepository;
    private final ParameterRepository parameterRepository;
    private final UserService userService;
    private final OfficialService officialService;
    private final DocumentService documentService;
    private final ParameterService parameterService;
    private final SignaturesService signaturesService;
    private final TypeOfDocumentService typeOfDocumentService;

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

    public List<BackQueue> getOfficialQueue(String login){
        System.out.println(login);
        List<Queue> queues = getQueueByOfficialUsername(login);
        List<BackQueue> qu = new ArrayList<>();
        BackQueue bq;
        for (Queue queue : queues) {
            bq = new BackQueue();
            User user = userService.getUserById(queue.getUserId());
            bq.setName(user.getName() + " " + user.getSurname());
            bq.setId(user.getId());
            bq.setPlace(queue.getPlace());
            bq.setPrior(queue.getPriority());
            qu.add(bq);
            System.out.println(user.getName());
        }
        return qu;
    }

    public FirstUser getFirstUserFromOfficialQueue(String login){
        System.out.println(login);
        User user = getFirstUserFromQueueByOfficialUsername(login);
        FirstUser firstUser = new FirstUser();
        firstUser.setId(user.getId());
        firstUser.setName(user.getName());
        firstUser.setSurname(user.getSurname());
        Long idSign;
        Official official = officialService.getOfficialByLogin(login);
        List<Document> documentList = documentService.getAllDocumentsByUserId(user.getId());
        List<Parameter> parameters = new ArrayList<>();
        Parameter parameter;
        for (Document document : documentList) {
            if (document.getParameters_id() != null) {
                parameter = parameterService.getByParametrId(document.getParameters_id());
                parameters.add(parameter);
            }
        }
        List<Signature> signatures;
        Signature sign = new Signature();
        for (Parameter value : parameters) {
            signatures = signaturesService.getSignsById(value.getId());
            for (Signature signature : signatures) {
                if (signature.getOfficialId().equals(official.getId()) && !signature.getIsSubscribed()) {
                    sign = signature;
                    break;
                }
            }
        }
        for (Document document : documentList) {
            if (document.getParameters_id() != null && document.getParameters_id().equals(sign.getParametersId())) {
                firstUser.setDockname(typeOfDocumentService.getById(document.getTypeOfDocumentId()).getName());
                break;
            }
        }
        return firstUser;
    }
}
