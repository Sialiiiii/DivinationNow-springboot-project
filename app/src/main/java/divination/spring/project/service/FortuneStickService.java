package divination.spring.project.service;

import divination.spring.project.model.JiaziSign;
import divination.spring.project.repository.JiaziSignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FortuneStickService {

    private final JiaziSignRepository repository;

    @Autowired
    public FortuneStickService(JiaziSignRepository repository) {
        this.repository = repository;
    }

    /**
     * 獲取所有籤詩數據
     */
    public List<JiaziSign> getAllSigns() {
        return repository.findAll();
    }

    /**
     * 根據籤號獲取特定籤詩
     */
    public Optional<JiaziSign> getSignByNumber(Integer signNumber) { // 🚀 修正類型
        return repository.findBySignNumber(signNumber);
    }
}
