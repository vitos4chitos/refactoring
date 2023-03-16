package main.database.repository;

import main.database.entity.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParameterRepository extends JpaRepository<Parameter, Long> {
    Parameter getParameterById(Long id);
}
