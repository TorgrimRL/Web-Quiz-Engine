package engine.repository

import engine.entity.Quiz
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface QuizRepository : JpaRepository<Quiz, Int> {

  @Modifying
  @Transactional
  @Query("DELETE FROM Quiz q WHERE q.id = :quizId AND q.user.id = :userId")
  fun deleteByIdAndUserId(quizId: Int, userId: Int): Int
}
