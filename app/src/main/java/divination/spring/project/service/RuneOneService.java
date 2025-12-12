package divination.spring.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import divination.spring.project.model.RuneOrientation;
import divination.spring.project.repository.RuneSingleLogRepository;

@Service
public class RuneOneService {
    private final RuneSingleLogRepository repository;

    @Autowired
    public RuneOneService(RuneSingleLogRepository repository) {
        this.repository = repository;
    }

    /**
     * 取得所有答案內容
     * @return 包含所有答案物件的列表
     */
    public List<RuneOrientation> getAllRuneOrientations() {
        return repository.findAll();
    }
}
