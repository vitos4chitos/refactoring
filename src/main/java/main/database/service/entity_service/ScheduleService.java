package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.database.entity.Schedule;
import main.database.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private Optional<Schedule> getScheduleById(Long id) {
        return scheduleRepository.getScheduleById(id);
    }

    public ResponseEntity<Schedule> getSchedule(Long id){
        log.info("Поступил запрос на поиск расписания id = {}", id);
        Optional<Schedule> schedule = getScheduleById(id);
        if (schedule.isPresent()){
            log.info("Расписание найдено: {}", schedule.get());
            return new ResponseEntity<>(schedule.get(), HttpStatus.OK);
        }
        log.error("Расписание не найдено");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
