package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import main.database.entity.Schedule;
import main.database.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public Schedule getScheduleById(Long id) {
        return scheduleRepository.getScheduleById(id);
    }
}
