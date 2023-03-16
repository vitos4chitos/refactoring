package main.database.service;

import main.controller.SecurityController;
import main.database.entity.User;
import main.database.repository.UserRepository;
import main.entity.AuthUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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
}
