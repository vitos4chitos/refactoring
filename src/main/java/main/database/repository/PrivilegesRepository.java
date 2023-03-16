package main.database.repository;

import main.database.entity.Privileges;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrivilegesRepository extends JpaRepository<Privileges, Long> {

    Privileges getPrivilegesById(Long id);
    List<Privileges> getPrivilegesBySale(int sale);
}
