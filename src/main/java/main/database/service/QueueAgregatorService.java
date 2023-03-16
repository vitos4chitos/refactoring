package main.database.service;

import lombok.RequiredArgsConstructor;
import main.database.entity.*;
import main.database.service.entity_service.*;
import main.entity.BackQueue;
import main.entity.FirstUser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QueueAgregatorService {

    private final UserService userService;
    private final DocumentService documentService;
    private final TypeOfDocumentService typeOfDocumentService;
    private final ParameterService parameterService;
    private final OfficialService officialService;
    private final QueueService queueService;
    private final SignaturesService signaturesService;

    public List<BackQueue> getOfficialQueue(String login){
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

    public FirstUser getFirstUserFromOfficialQueue(String login){
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
                parameter = parameterService.getByParameterId(document.getParameters_id());
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
