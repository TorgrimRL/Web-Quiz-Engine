package engine.service

import engine.dto.QuizQuestionResponse
import engine.dto.QuizResponse
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.OverrideMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import engine.repository.QuizRepository
import engine.repository.UserRepository
import engine.repository.QuizCompletionsRepository
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@ExtendWith(MockKExtension::class)
class QuizServiceTest {
    @MockK
    private lateinit var quizRepository: QuizRepository

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var quizCompletionsRepository: QuizCompletionsRepository

    @OverrideMockKs
    private lateinit var quizService: QuizService

    companion object {
        fun expectedSampleQuiz(): QuizQuestionResponse = QuizQuestionResponse(
                "The Java Logo",
                "What is depicted on the Java logo?",
                listOf("Robot", "Tea leaf", "Cup of coffee", "Bug"))

        fun checkAnswerCorrectAnswer(): QuizResponse = QuizResponse(true, "Congratulations, you're right!")
        fun checkAnswerWrongAnswer(): QuizResponse = QuizResponse(false, "Wrong answer! Please, try again.")
    }

    @Test
    fun `getSampleQuiz should return the hardcoded sample response`() {
        val result = quizService.getSampleQuiz()
        assertEquals(expectedSampleQuiz(), result)
    }

    @Test
    fun `checkAnswer should response with correct answer`() {
        val result = quizService.checkAnswer(2)
        assertEquals(checkAnswerCorrectAnswer(), result)
    }
    @Test
    fun `checkAnswer response with wrong answer`() {
        val result = quizService.checkAnswer(1)
        assertEquals(checkAnswerWrongAnswer(), result)
    }
}