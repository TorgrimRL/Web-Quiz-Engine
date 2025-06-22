package engine.service

import engine.dto.QuizQuestionResponse
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

    }

    @Test
    fun `getSampleQuiz should return the hardcoded sample response`() {
        val result = quizService.getSampleQuiz()
        assertEquals(expectedSampleQuiz(), result)
    }
}