package main.controller;


import main.entity.*;
import main.repository.DocumentRepository;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/queue")
public class QueueController {

    @Autowired
    QueueService queueService;

    @Autowired
    UserService userService;

    @Autowired
    OfficialService officialService;

    @Autowired
    DocumentService documentService;

    @Autowired
    SignaturesService signaturesService;

    @Autowired
    ParameterService parameterService;

    @Autowired
    TypeOfDocumentService typeOfDocumentService;

    @GetMapping("/queue/get")
    String getQueue(@RequestParam Long id) {
        return queueService.getQueueById(id).toString();
    }

    @GetMapping("/queue/schedule/get")
    String getSchedule(@RequestParam Long id) {
        return queueService.getScheduleById(id).toString();
    }

    @GetMapping("/queue/getAll")
    List<Queue> getQueueAllByUserId(@RequestParam Long id) {
        return queueService.getAllQueueByUserId(id);
    }

    @GetMapping("/getUserQueue")
    List<QueueBack> getQueueByUser(@RequestParam String login){
        List<QueueBack> queueBacks = new ArrayList<>();
        long id = userService.getUserId(login);
        List<Queue> queues = queueService.getAllQueueByUserId(id);
        for(int i = 0; i < queues.size(); i++){
            QueueBack qb = new QueueBack();
            qb.setId(queues.get(i).getId());
            Official official = officialService.getOfficialById(queues.get(i).getOfficialId());
            qb.setName(official.getName() + " " + official.getSurname());
            System.out.println(qb.getName());
            qb.setPlace(queues.get(i).getPlace());
            qb.setPriority(queues.get(i).getPriority());
            qb.setTime(official.getTimeOfReceipts().toString());
            queueBacks.add(qb);
        }
        return queueBacks;
    }

    @GetMapping("official")
    public List<BackQueue> getOfficialQueue(@RequestParam String login) {
        System.out.println(login);
        List<Queue> queues = queueService.getQueueByOfficialUsername(login);
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

    @GetMapping("/firstUser")
    public FirstUser getFirstUserFromOfficialQueue(@RequestParam String login) {
        System.out.println(login);
        User user = queueService.getFirstUserFromQueueByOfficialUsername(login);
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

    @GetMapping("/queue/advance")
    public Boolean getAdvanceQueue(@RequestParam Long officialId) {
        return queueService.advanceQueue(officialId);
    }
}
