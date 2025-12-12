package divination.spring.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import divination.spring.project.model.BookOfAnswers;

/**
 * 處理 book_of_answers 資料表的 CRUD 操作
 */
@Repository
public interface BookOfAnswersRepository extends JpaRepository<BookOfAnswers, Long> {

    /**
     * 獲取所有解答之書的答案
     * JpaRepository 已經提供了 findAll() 方法，我們無需額外定義。
     */
}
