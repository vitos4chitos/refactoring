package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.database.entity.*;
import main.database.repository.*;
import main.entity.responce.BaseAnswer;
import main.entity.responce.ErrorAnswer;
import main.entity.responce.UserInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstanceService {

    private final ProsecutorRepository prosecutorRepository;
    private final InstanceRepository instanceRepository;
    private final CheckerRepository checkerRepository;

    private final UserService userService;

    private Optional<Prosecutor> getProsecutorId(Long id) {
        return prosecutorRepository.getProsecutorById(id);
    }

    public ResponseEntity<Prosecutor> getProsecutor(Long id){
        log.info("Поступил запрос на получение prosecutor id = {}", id);
        Optional<Prosecutor> optionalProsecutor = getProsecutorId(id);
        if(optionalProsecutor.isPresent()){
            log.info("Нужный prosecutor найден: {}", optionalProsecutor.get());
            return new ResponseEntity<>(optionalProsecutor.get(), HttpStatus.OK);
        }
        log.error("Нужный prosecutor не найден");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }



    private Optional<Instance> getInstanceId(Long id) {
        return instanceRepository.getInstanceById(id);
    }

    public ResponseEntity<Instance> getInstance(Long id){
        log.info("Поступил запрос на получение инстанции id = {}", id);
        Optional<Instance> optional = getInstanceId(id);
        if(optional.isPresent()){
            log.info("Нужный инстанция найдена: {}", optional.get());
            return new ResponseEntity<>(optional.get(), HttpStatus.OK);
        }
        log.error("Нужный инстанция не найдена");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }



    private Optional<Checker> getCheckerId(Long id) {
        return checkerRepository.getCheckerById(id);
    }

    public ResponseEntity<Checker> getChecker(Long id){
        log.info("Поступил запрос на получение Checker id = {}", id);
        Optional<Checker> optional = getCheckerId(id);
        if(optional.isPresent()){
            log.info("Нужный Checker найден: {}", optional.get());
            return new ResponseEntity<>(optional.get(), HttpStatus.OK);
        }
        log.error("Нужный Checker не найден");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<BaseAnswer> transferToTheNextLevelCheck(String login) {
        log.info("Поступил запрос на перевод пользователя на следующую инстанцию login = {}",
                login);
        Optional<User> user = userService.getUserByLogin(login);
        if (user.isPresent()) {
            boolean bool =  instanceRepository.transferToTheNextLevel(user.get().getId());
            if(bool){
                log.info("Пользователь переведен на следующую инстанцию");
                return new ResponseEntity<>(HttpStatus.OK);
            }
            log.info("Недостаточно документов");
            return new ResponseEntity<>(new ErrorAnswer("No match documents"), HttpStatus.BAD_REQUEST);

        }
        log.info("Пользователь не найден");
        return new ResponseEntity<>(new ErrorAnswer("UserNotFound"), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public ResponseEntity<BaseAnswer> getInstance(String login) {
        log.info("Поступил запрос на получение информации о деньгах и инстанции пользователя login =  {}", login);
        Long user = userService.getUserId(login);
        if (user == -1) {
            log.error("Пользователь не найден");
            return new ResponseEntity<>(ErrorAnswer.builder()
                    .message("UserNotFound")
                    .build(),
                    HttpStatus.BAD_REQUEST);
        } else {
            User myUser = userService.getUserById(user);
            log.info("Удалось найти пользователя");
            return new ResponseEntity<>(
                    UserInfo.builder()
                        .instanceId(myUser.getInstanceId())
                        .userMoney(myUser.getMoney())
                        .build(),
                    HttpStatus.OK);
        }
    }

    public ResponseEntity<BaseAnswer> transferToTheNextLevel(String login) {
        return transferToTheNextLevelCheck(login);
    }

}
