package divination.spring.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import divination.spring.project.model.RuneOrientation;
import divination.spring.project.repository.RuneOrientationRepository;

@Service
public class RuneOneService {
    private final RuneOrientationRepository repository;

    @Autowired
    public RuneOneService(RuneOrientationRepository repository) {
        this.repository = repository;
    }

    /**
     * 取得所有答案內容
     * @return 包含所有答案物件的列表
     */
    public List<RuneOrientation> getAllRuneOrientations() {
        // 直接使用 JpaRepository 提供的 findAll() 方法
        return repository.findAll();
    }
}
