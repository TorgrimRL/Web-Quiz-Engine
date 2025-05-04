package engine.service

import engine.repository.QuizCompletionsRepository
import engine.repository.QuizRepository
import engine.repository.UserRepository
import engine.dto.PostQuizRequest
import engine.dto.QuizAnswerRequest
import engine.dto.QuizQuestionResponse
import engine.dto.QuizResponse
import engine.entity.Quiz
import engine.entity.QuizCompletion
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class QuizService(
    private val quizRepository: QuizRepository,
    private val userRepository: UserRepository,
    private val quizCompletionsRepository: QuizCompletionsRepository) {


    fun getSampleQuiz(): QuizQuestionResponse = QuizQuestionResponse(
            "The Java Logo", "What is depicted on the Java logo?", listOf("Robot", "Tea leaf", "Cup of coffee", "Bug"))

    fun checkAnswer(submittedAnswer: Int): QuizResponse {
        return if (submittedAnswer == 2) {
            QuizResponse(true, "Congratulations, you're right!")
        } else {
            QuizResponse(false, "Wrong answer! Please, try again.")
        }
    }

    fun addQuiz(postQuizRequest: PostQuizRequest, email: String): Quiz {
        val currentUser = userRepository.findUserByEmail(email) ?: throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "User not found")
        val saved = quizRepository.save(
                Quiz(
                        title = postQuizRequest.title!!,
                        text = postQuizRequest.text!!,
                        options = postQuizRequest.options,
                        answer = postQuizRequest.answer ?: emptyList()).apply { user = currentUser })
        return saved
    }

    fun getPageOfQuizzes(page: Int): Page<Quiz> = quizRepository.findAll(PageRequest.of(page, 10))

    fun getQuizById(id: Int): Quiz {
        val quiz =
            quizRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found")
        return quiz

    }

    fun proccesAnswer(id: Int, answer: QuizAnswerRequest, email: String): QuizResponse {
        val quiz = quizRepository.findByIdOrNull(id) ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND, "Did not find quiz")
        return if (quiz.answer.toList() == answer.answer) {
            val currentUser = userRepository.findUserByEmail(email)
            quizCompletionsRepository.save(QuizCompletion(quiz = quiz, user = currentUser))
            QuizResponse(true, "Congratulations, you're right! ")
        } else {
            QuizResponse(
                    false, "Wrong answer! Please, try again.")
        }
    }

    @Transactional
    fun deleteQuiz(id: Int, email: String) {
        val currentUser =
            userRepository.findUserByEmail(email) ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED)

        val quiz =
            quizRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found")
        if (quiz.user?.id != currentUser.id) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "You cant access other peoples quizzes")
        }
        quizCompletionsRepository.deleteAllByQuiz(quiz)
        quizRepository.deleteByIdAndUserId(id, currentUser.id!!)
    }

    fun getUserSpecificCompletedQuizzesPage(page: Int, email: String): Page<QuizCompletion> {
        val currentUser =
            userRepository.findUserByEmail(email) ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        val pageResult = quizCompletionsRepository.findQuizCompletionByUserOrderByCompletedAtDesc(
                currentUser, PageRequest.of(page, 10))
        return pageResult
    }
}