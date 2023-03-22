package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.database.entity.*;
import main.database.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookkeepingService {

    private final BookkeepingRepository bookkeepingRepository;

    public ResponseEntity<Bookkeeping> getBookkeepingById(Long bookkeepingId) {
        log.info("Поступил запрос на получение бухалтерии id = {}", bookkeepingId);
        Optional<Bookkeeping> optional = bookkeepingRepository.getBookkeepingById(bookkeepingId);
        if(optional.isPresent()){
            log.info("Бухалтерия найдена {}", optional.get());
            return new ResponseEntity<>(optional.get(), HttpStatus.OK);
        }
        log.error("Бухалтерия не найдена");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
