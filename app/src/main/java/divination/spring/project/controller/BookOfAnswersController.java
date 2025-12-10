package divination.spring.project.controller;

import divination.spring.project.model.BookOfAnswers;
import divination.spring.project.service.BookOfAnswersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 解答之書 API
 */
@RestController
@RequestMapping("/divination")
public class BookOfAnswersController {

    private final BookOfAnswersService service;

    public BookOfAnswersController(BookOfAnswersService service) {
        this.service = service;
    }

    /**
     * GET /api/divination/bookofanswers
     * 獲取所有籤詩內容
     */
    @GetMapping("/bookofanswers")
    public ResponseEntity<List<BookOfAnswers>> getBookAnswers() {
        List<BookOfAnswers> answers = service.getAllAnswers();
        return ResponseEntity.ok(answers);
    }
}