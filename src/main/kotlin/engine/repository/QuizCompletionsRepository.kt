package engine.repository

import engine.entity.Quiz
import engine.entity.QuizCompletion
import engine.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface QuizCompletionsRepository : JpaRepository<QuizCompletion, Int> {
  fun findQuizCompletionByUserOrderByCompletedAtDesc(
      user: User?,
      pageable: Pageable
  ): Page<QuizCompletion>

  @Modifying
  @Transactional
  @Query("DELETE FROM QuizCompletion qc WHERE qc.quiz = :quiz")
  fun deleteAllByQuiz(quiz: Quiz): Int
}
