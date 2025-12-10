package divination.spring.project.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import divination.spring.project.model.SpecificRuneReading;
import divination.spring.project.repository.SpecificRuneReadingRepository;

@Service
public class RuneTwoService {

    private final SpecificRuneReadingRepository readingRepository;

    public RuneTwoService(SpecificRuneReadingRepository readingRepository) {
        this.readingRepository = readingRepository;
    }

    /**
     * 獲取特定情境下的符文解讀
     */
    public Optional<SpecificRuneReading> getSpecificRuneReading(Integer orientationId, Integer statusId, Integer position) {
        return readingRepository.findByOrientationIdAndUserStatusIdAndIsCurrentStatusPosition(
                orientationId, statusId, position);
    }
}