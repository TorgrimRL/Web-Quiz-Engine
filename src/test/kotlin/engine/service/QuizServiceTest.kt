package engine.service

import engine.entity.Quiz
import engine.repository.QuizCompletionsRepository
import engine.repository.QuizRepository
import engine.repository.UserRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.OverrideMockKs
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

@ExtendWith(MockKExtension::class)
class QuizServiceTest {
  @MockK private lateinit var quizRepository: QuizRepository

  @MockK private lateinit var userRepository: UserRepository

  @MockK private lateinit var quizCompletionsRepository: QuizCompletionsRepository

  @OverrideMockKs private lateinit var quizService: QuizService

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

  @Test
  fun `addQuiz should return unauhorized if user not found`() {
    every { userRepository.findUserByEmail(any()) } returns null
    val ex =
        assertThrows(ResponseStatusException::class.java) {
          quizService.addQuiz(QuizTestData.postQuizRequest(), "123@hotmail.com")
        }
    assertEquals(HttpStatus.UNAUTHORIZED, ex.statusCode)
    verify(exactly = 0) { quizRepository.save(any()) }
  }

  @Test
  fun `addQuiz should save a correctly requested quiz`() {
    val post = QuizTestData.postQuizRequest()
    val email = "123@hotmail.com"
    val user = QuizTestData.sampleUser() // Fixed typo here

    every { userRepository.findUserByEmail(any()) } returns user
    every { quizRepository.save(any()) } answers { firstArg<Quiz>() }
    val result = quizService.addQuiz(post, email)
    verify(exactly = 1) { userRepository.findUserByEmail(email) }

    val slot = slot<Quiz>()
    verify(exactly = 1) { quizRepository.save(capture(slot)) }
    assertEquals(post.title, slot.captured.title)
    assertEquals(post.text, slot.captured.text)
    assertEquals(post.options, slot.captured.options)
    assertEquals(post.answer, slot.captured.answer)
    assertSame(user, slot.captured.user)
    assertSame(slot.captured, result)
  }
}
