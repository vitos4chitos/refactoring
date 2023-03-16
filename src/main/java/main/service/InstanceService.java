package main.service;

import main.entity.*;
import main.repository.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InstanceService {

    private final ProsecutorRepository prosecutorRepository;
    private final OfficialRepository officialRepository;
    private final InstanceRepository instanceRepository;
    private final CheckerRepository checkerRepository;
    private final UserRepository userRepository;

    public InstanceService(ProsecutorRepository prosecutorRepository, OfficialRepository officialRepository, InstanceRepository instanceRepository, CheckerRepository checkerRepository, UserRepository userRepository) {
        this.prosecutorRepository = prosecutorRepository;
        this.officialRepository = officialRepository;
        this.instanceRepository = instanceRepository;
        this.checkerRepository = checkerRepository;
        this.userRepository = userRepository;
    }

    public Prosecutor getProsecutorId(Long id) {
        return prosecutorRepository.getProsecutorById(id);
    }
    public Official getOfficialById(Long id) {
        return officialRepository.getOfficialById(id);
    }

    public Instance getInstanceId(Long id) {
        return instanceRepository.getInstanceById(id);
    }

    public Checker getCheckerId(Long id) {
        return checkerRepository.getCheckerById(id);
    }

    public Boolean transferToTheNextLevel(String login) {
        Optional<User> user = userRepository.getUserByLogin(login);
        if (user.isPresent()) {
            User user1 = user.get();
            return instanceRepository.transferToTheNextLevel(user1.getId());
        }
        return false;
    }

}
