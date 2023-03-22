package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.database.entity.User;
import main.database.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Long getUserId(String login) {
        Optional<User> userOptional = userRepository.getUserByLogin(login);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            log.info("Найден пользователь: {}", user);
            return user.getId();
        }
        log.error("Пользователь с логином {} не найден", login);
        return (long) -1;
    }

    public User getUserById(long id) {
        return userRepository.getUserById(id);
    }

    public Optional<User> getUserByLogin(String login) {
        return userRepository.getUserByLogin(login);
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
