package main.controller;


import main.entity.*;
import main.repository.DocumentRepository;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("{id}")
    String getQueue(@PathVariable("id") Long id) {
        return queueService.getQueueById(id).toString();
    }

    @GetMapping("/user/{id}")
    List<Queue> getUsersQueues(@PathVariable("id") Long id) {
        return queueService.getAllQueueByUserId(id);
    }

    @GetMapping("/official/{login}")
    public List<BackQueue> getOfficialQueue(@PathVariable("login") String login) {
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

    @GetMapping("/official/{login}/first-user")
    public FirstUser getFirstUserFromOfficialQueue(@PathVariable("login") String login) {
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

    @GetMapping("/official/{login}/advance")
    public Boolean getAdvanceQueue(@PathVariable("login") Long officialId) {
        return queueService.advanceQueue(officialId);
    }
}
