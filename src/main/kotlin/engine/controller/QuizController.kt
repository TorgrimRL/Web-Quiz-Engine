package engine.controller

import engine.service.QuizService
import engine.dto.PostQuizRequest
import engine.dto.QuizAnswerRequest
import engine.entity.Quiz
import engine.entity.QuizCompletion
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RequestMapping("/api")
@RestController
class QuizController(
    private val quizService: QuizService) {

    @GetMapping("/quiz")
    fun getQuiz(): ResponseEntity<Any> {
        val quizQuestion = quizService.getSampleQuiz()
        return ResponseEntity.ok(quizQuestion)
    }

    @PostMapping("/quiz")
    fun postAnswer(answer: Int): ResponseEntity<Any> {
        val result = quizService.checkAnswer(answer)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/quizzes")
    fun addQuiz(
        @RequestBody @Valid postQuizRequest: PostQuizRequest,
        @AuthenticationPrincipal details: UserDetails): ResponseEntity<Quiz> {
        val saved = quizService.addQuiz(postQuizRequest, details.username)
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(saved)
    }


    @GetMapping("/quizzes")
    fun getQuizzes(
        @RequestParam("page", defaultValue = "0") page: Int): Page<Quiz> {
        val result = quizService.getPageOfQuizzes(page)
        return result
    }


    @GetMapping("/quizzes/{id}")
    fun getQuiz(@PathVariable("id") @Min(1) id: Int): Any {
        val result = quizService.getQuizById(id)
        return result
    }

    @PostMapping("/quizzes/{id}/solve")
    fun answerQuiz(
        @PathVariable("id") @Min(1) id: Int,
        @RequestBody @Valid answer: QuizAnswerRequest,
        @AuthenticationPrincipal details: UserDetails): Any {
        val result = quizService.proccesAnswer(id, answer, details.username)
        return result
    }

    @DeleteMapping("/quizzes/{id}")
    fun deleteQuiz(
        @PathVariable id: Int, @AuthenticationPrincipal details: UserDetails): ResponseEntity<Void> {
        quizService.deleteQuiz(id, details.username!!)
        return ResponseEntity.noContent().build()
    }


    @GetMapping("/quizzes/completed")
    fun getCompletedQuizzes(
        @RequestParam("page", defaultValue = "0") page: Int,
        @AuthenticationPrincipal details: UserDetails): Page<QuizCompletion> {
        val result = quizService.getUserSpecificCompletedQuizzesPage(page, details.username!!)
        return result
    }
}