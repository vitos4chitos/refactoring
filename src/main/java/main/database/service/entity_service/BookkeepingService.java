package main.database.service.entity_service;

import lombok.RequiredArgsConstructor;
import main.database.entity.*;
import main.database.repository.*;
import main.entity.BackVals;
import main.entity.Shop;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookkeepingService {

    private final BookkeepingRepository bookkeepingRepository;

    public Bookkeeping getBookkeepingById(Long bookkeepingId) {
        return bookkeepingRepository.getBookkeepingById(bookkeepingId);
    }

}
