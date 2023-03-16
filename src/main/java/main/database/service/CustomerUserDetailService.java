package main.database.service;

import main.database.entity.Official;
import main.database.entity.User;
import main.database.repository.OfficialRepository;
import main.database.repository.UserRepository;
import main.entity.AuthUser;
import main.entity.RegUserForm;
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
public class CustomerUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private final OfficialRepository officialRepository;

    public CustomerUserDetailService(UserRepository userRepository, OfficialRepository officialRepository) {
        this.userRepository = userRepository;
        this.officialRepository = officialRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepository.getUserByLogin(login);
        if(!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        User user = userOptional.get();
        System.out.println(user.toString());
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("USER"));

        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), authorities);
    }

    public Official loadUserByUsernameO(String login) throws UsernameNotFoundException {

        Official officialOptional = officialRepository.getOfficialByLogin(login);
        if(officialOptional == null) {
            throw new UsernameNotFoundException("Official not found");
        }
        return officialOptional;
    }

    public String addUser(User user) {
        try {
            user.setRole("USER");
            userRepository.save(user);
            return "{\"token\": \"true\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"token\": \"err\"}";
        }
    }

    public String auth(AuthUser user){
        System.out.println("****");
        System.out.println(user.getLogin());
        UserDetails securityUser = loadUserByUsername(user.getLogin());
        if (securityUser != null) {
            System.out.println(securityUser.getPassword());
            if (securityUser.getPassword().equals(user.getPassword())) {
                System.out.println("User exist");
                return "{\"token\": \"true\"}";
            }
            return "{\"token\": \"bad\"}";
        } else {
            System.out.println("User doesn't exist");
            System.out.println("POST request ... ");
            return "{\"token\": \"bad\"}";
        }
    }

    public String singUp(RegUserForm user) throws ParseException {
        System.out.println("sdsdsd");
        User us = new User();
        us.setInstance_id((long) 1);
        us.setActive(user.isActive());
        us.setLogin(user.getUsername());
        us.setMoney(user.getMoney());
        us.setName(user.getName());
        String hashPassword = new BCryptPasswordEncoder(12).encode(user.getPassword());
        us.setPassword(hashPassword);
        us.setRole(user.getRole());
        us.setSurname(user.getSurname());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(user.getDate().toString());
        long time = date.getTime();
        us.setTime_result(new Timestamp(time));
        System.out.println(us.toString());
        return addUser(us);
    }

    public String authOfficial(AuthUser user){
            System.out.println("****");
            System.out.println(user.getLogin());
            Official official = loadUserByUsernameO(user.getLogin());
            System.out.println(official.getName() + " " + official.getSurname());
            System.out.println(official.getPassword());
            if (official.getPassword().equals(user.getPassword())) {
                System.out.println("User exist");
                return "{\"token\": \"true\"}";
            }
            return "{\"token\": \"bad\"}";
        }
}
