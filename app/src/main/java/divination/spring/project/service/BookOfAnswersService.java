package divination.spring.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import divination.spring.project.model.BookOfAnswers;
import divination.spring.project.repository.BookOfAnswersRepository;

/**
 * 處理解答之書的業務邏輯
 */
@Service
public class BookOfAnswersService {

    private final BookOfAnswersRepository repository;

    @Autowired
    public BookOfAnswersService(BookOfAnswersRepository repository) {
        this.repository = repository;
    }

    /**
     * 取得所有答案內容
     * @return 包含所有答案物件的列表
     */
    public List<BookOfAnswers> getAllAnswers() {
        // 直接使用 JpaRepository 提供的 findAll() 方法
        return repository.findAll();
    }
}