package divination.spring.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import divination.spring.project.model.BookOfAnswers;
import divination.spring.project.repository.BookOfAnswersRepository;

/**
 * 解答之書的業務邏輯
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
     * @return
     */
    public List<BookOfAnswers> getAllAnswers() {
        return repository.findAll();
    }
}