package main.service;

import main.entity.User;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getUserName(Long id) {
        User user = userRepository.getUserById(id);
        if (user != null) {
            return user.getName();
        }
        return "test fail";
    }

    public Long getUserId(String login){
        Optional<User> userOptional = userRepository.getUserByLogin(login);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            return user.getId();
        }
        return (long) -1;
    }
    public User getUserById(long id){
        User user = userRepository.getUserById(id);
        return user;
    }


    // to do получать у юзера все документы, все очереди и все что купить заказать в магазине
}
