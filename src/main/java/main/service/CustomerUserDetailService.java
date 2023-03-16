package main.service;

import main.entity.Official;
import main.entity.User;
import main.repository.OfficialRepository;
import main.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
}
