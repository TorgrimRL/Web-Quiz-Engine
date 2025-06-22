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
import engine.service.QuizTestData.checkAnswerCorrectAnswer
import engine.service.QuizTestData.expectedSampleQuiz
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

    @Test
    fun `getSampleQuiz should return the hardcoded sample response`() {
        val expected = QuizTestData.expectedSampleQuiz()
        val result = quizService.getSampleQuiz()
        assertEquals(expected, result)
    }

    @Test
    fun `checkAnswer should response with correct answer`() {
        val expected = QuizTestData.checkAnswerCorrectAnswer()
        val result = quizService.checkAnswer(2)
        assertEquals(expected, result)
    }

    @Test
    fun `checkAnswer response with wrong answer`() {
        val expected = QuizTestData.checkAnswerWrongAnswer()
        val result = quizService.checkAnswer(1)
        assertEquals(expected, result)
    }
}