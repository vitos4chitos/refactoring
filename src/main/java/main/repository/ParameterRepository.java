package main.repository;

import main.entity.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParameterRepository extends JpaRepository<Parameter, Long> {
    Parameter getParameterById(Long id);
}
