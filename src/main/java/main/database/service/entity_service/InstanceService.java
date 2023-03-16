package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import main.database.entity.*;
import main.database.repository.*;
import main.entity.UserInfo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstanceService {

    private final ProsecutorRepository prosecutorRepository;
    private final InstanceRepository instanceRepository;
    private final CheckerRepository checkerRepository;

    private final UserService userService;

    public Prosecutor getProsecutorId(Long id) {
        return prosecutorRepository.getProsecutorById(id);
    }

    public Instance getInstanceId(Long id) {
        return instanceRepository.getInstanceById(id);
    }

    public Checker getCheckerId(Long id) {
        return checkerRepository.getCheckerById(id);
    }

    public Boolean transferToTheNextLevelCheck(String login) {
        Optional<User> user = userService.getUserByLogin(login);
        if (user.isPresent()) {
            User user1 = user.get();
            return instanceRepository.transferToTheNextLevel(user1.getId());
        }
        return false;
    }

    public UserInfo getInstance(String login){
        Long user = userService.getUserId(login);
        if(user == -1){
            return null;
        }
        else{
            UserInfo userInfo = new UserInfo();
            userInfo.setId(userService.getUserById(user).getInstance_id());
            userInfo.setMoney(userService.getUserById(user).getMoney());
            System.out.println(userInfo.getId());
            return userInfo;
        }
    }

    public String transferToTheNextLevel(String login) {
        boolean a = transferToTheNextLevelCheck(login);
        if(a){
            return "{\"token\": \"true\"}";
        }
        else{
            return "{\"token\": \"false\"}";
        }
    }

}
