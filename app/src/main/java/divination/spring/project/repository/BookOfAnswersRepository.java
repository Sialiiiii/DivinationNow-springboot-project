package divination.spring.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import divination.spring.project.model.BookOfAnswers;

/**
 * book_of_answers 資料表的 CRUD 操作
 */
@Repository
public interface BookOfAnswersRepository extends JpaRepository<BookOfAnswers, Long> {

}
