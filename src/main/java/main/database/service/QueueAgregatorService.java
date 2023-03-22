package main.database.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.database.entity.*;
import main.database.service.entity_service.*;
import main.entity.responce.queue.BackQueue;
import main.entity.responce.BaseAnswer;
import main.entity.responce.ErrorAnswer;
import main.entity.responce.FirstUser;
import main.entity.responce.queue.UserInQueue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueAgregatorService {

    private final UserService userService;
    private final DocumentService documentService;
    private final TypeOfDocumentService typeOfDocumentService;
    private final ParameterService parameterService;
    private final OfficialService officialService;
    private final QueueService queueService;
    private final SignaturesService signaturesService;

    public ResponseEntity<BaseAnswer> getOfficialQueue(String login) {
        log.info("Поступил запрос на получение очереди у оф.лица login = {}", login);
        Official official = officialService.getOfficialByLogin(login);
        if(official == null){
            return new ResponseEntity<>(new ErrorAnswer("OfficialNotFound"), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        log.info("Формирую очердь");
        List<Queue> queues = queueService.getQueueByOfficialUsername(official);
        BackQueue bq = BackQueue.builder().queue(new ArrayList<>()).build();
        queues.forEach(q -> {
            User user = userService.getUserById(q.getUserId());
            bq.addUser(UserInQueue.builder()
                    .name(user.getName() + " " + user.getSurname())
                    .id(user.getId())
                    .place(q.getPlace())
                    .prior(q.getPriority())
                    .build());
        });
        log.info("Сформирована очередь {}", bq);
        return new ResponseEntity<>(bq, HttpStatus.OK);
    }

    public ResponseEntity<BaseAnswer> getFirstUserFromOfficialQueue(String login) {
        log.info("Поступил запрос на получение первого полльзователя в очереди у оф.лица login = {}", login);
        Official official = officialService.getOfficialByLogin(login);
        if(official == null){
            return new ResponseEntity<>(new ErrorAnswer("OfficialNotFound"), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Long userId = queueService.getFirstUserIdFromQueueByOfficialUsername(official);
        if(userId.equals(-1L)){
            log.info("Очередь пустая");
            return new ResponseEntity<>(new BaseAnswer(), HttpStatus.OK);
        }
        User user = userService.getUserById(userId);
        FirstUser firstUser = FirstUser
                .builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .build();
        List<Document> documentList = documentService.getAllDocumentsByUserId(user.getId());
        List<Parameter> parameters = new ArrayList<>();
        log.info("Формирование параметров");
        documentList.stream()
                .filter(d -> d.getParametersId() != null)
                .forEach(d -> parameters.add(parameterService.getByParameterId(d.getParametersId())));
        List<Signature> signatures;
        log.info("Формирование подписей");
        Signature sign = new Signature();
        for (Parameter value : parameters) {
            signatures = signaturesService.getSignsById(value.getId());
            Optional<Signature> signature = signatures.stream()
                    .filter(s -> s.getOfficialId().equals(official.getId()) && !s.getIsSubscribed())
                    .findFirst();
            if(signature.isPresent()){
                sign = signature.get();
            }
        }
        log.info("Формирование документа на подпись");
        for (Document document : documentList) {
            if (document.getParametersId() != null && document.getParametersId().equals(sign.getParametersId())) {
                firstUser.setDockname(typeOfDocumentService.getById(document.getTypeOfDocumentId()).getName());
                break;
            }
        }
        log.info("Формирование ответа");

        return new ResponseEntity<>(firstUser, HttpStatus.OK);
    }
}
