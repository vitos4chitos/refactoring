package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.database.entity.Official;
import main.database.entity.User;
import main.database.repository.OfficialRepository;
import main.database.repository.UserRepository;
import main.entity.AuthUser;
import main.entity.BaseAnswer;
import main.entity.ErrorAnswer;
import main.entity.RegUserForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private final OfficialRepository officialRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepository.getUserByLogin(login);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        User user = userOptional.get();
        System.out.println(user);
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("USER"));

        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), authorities);
    }

    private Official loadUserOffByUsername(String login) throws UsernameNotFoundException {

        Optional<Official> officialOptional = officialRepository.getOfficialByLogin(login);
        if (!officialOptional.isPresent()) {
            throw new UsernameNotFoundException("Official not found");
        }
        return officialOptional.get();
    }

    public Boolean addUser(User user) {
        try {
            user.setRole("USER");
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            log.error(e.toString());
            return false;
        }
    }

    public ResponseEntity<BaseAnswer> auth(AuthUser user) {
        log.info("Постпуил запрос на авторизацию: {}", user);
        UserDetails securityUser = loadUserByUsername(user.getLogin());
        if (securityUser != null) {
            log.info(securityUser.getPassword());
            if (securityUser.getPassword().equals(user.getPassword())) {
                log.info("Пользовательские данные верны");
                return new ResponseEntity<>(HttpStatus.OK);
            }
            log.error("Пользовательские данные не верны");
            return new ResponseEntity<>(ErrorAnswer.builder()
                    .message("incorrect login or password")
                    .build(), HttpStatus.FORBIDDEN);
        } else {
            log.error("Пользователя не существует");
            return new ResponseEntity<>(ErrorAnswer.builder()
                    .message("UserNotFound")
                    .build(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<BaseAnswer> singUp(RegUserForm user) throws ParseException {
        log.info("Поступил запрос на регистрацию пользователя {}", user);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(user.getDate().toString());
        long time = date.getTime();
        User us = User.builder()
                .instanceId(1L)
                .active(user.isActive())
                .login(user.getUsername())
                .money(user.getMoney())
                .name(user.getName())
                .role(user.getRole())
                .password(user.getPassword())
                .surname(user.getSurname())
                .time_result(new Timestamp(time))
                .build();
        if(addUser(us)){
            log.info("Успешная регистрация {}", user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        log.error("Пользователь {} не смог зарегистрироваться", user);
        return new ResponseEntity<>(ErrorAnswer.builder()
                .message("Bad Request").build(),
                HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<BaseAnswer> authOfficial(AuthUser user) {
        log.info("Постпуил запрос на авторизацию оф.лица: {}", user);
        Official official = loadUserOffByUsername(user.getLogin());
        if (official.getPassword().equals(user.getPassword())) {
            log.info("Пользовательские данные верны");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(ErrorAnswer.builder()
                .message("incorrect login or password")
                .build(), HttpStatus.FORBIDDEN);
    }
}
