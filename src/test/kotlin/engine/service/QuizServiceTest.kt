package engine.service

import engine.dto.QuizQuestionResponse
import engine.dto.QuizResponse
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
import org.junit.jupiter.api.assertAll
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
    val title = "The Java Logo"
    val question = "What is depicted on the Java logo?"
    val alternatives = listOf("Robot", "Tea leaf", "Cup of coffee", "Bug")
    Given(
            thing =
                QuizQuestionResponse(
                    title = title,
                    text = question,
                    options = alternatives,
                ),
        )
        .When { quizService.getSampleQuiz() }
        .Then {
          val expectedResult =
              QuizQuestionResponse(
                  title = title,
                  text = question,
                  options = alternatives,
              )
          assertEquals(expectedResult, it)
        }
  }

  @Test
  fun `checkAnswer should response with correct answer`() {
    val answer = 2
    Given(answer)
        .When { quizService.checkAnswer(answer) }
        .Then { result ->
          val expectedSuccess = true
          val expectedFeedback = "Congratulations, you're right!"
          val expectedResponse = QuizResponse(expectedSuccess, expectedFeedback)
          assertAll(
              { assertEquals(expectedResponse, result) },
              { assertEquals(expectedSuccess, result.success) },
              { assertEquals(expectedFeedback, result.feedback) },
          )
        }
  }

  @Test
  fun `checkAnswer response with wrong answer`() {
    val answer = 1
    Given(answer)
        .When { quizService.checkAnswer(answer) }
        .Then { result ->
          val expectedSuccess = false
          val expectedFeedback = "Wrong answer! Please, try again."
          val expectedResponse = QuizResponse(expectedSuccess, expectedFeedback)
          assertAll(
              { assertEquals(expectedResponse, result) },
              { assertEquals(expectedSuccess, result.success) },
              { assertEquals(expectedFeedback, result.feedback) },
          )
        }
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

private fun <T> Given(thing: T): T = thing

private inline fun <T, R> T.When(block: (T) -> R): R = this.let(block)

private inline fun <T, R> T.Then(block: (T) -> R): R = this.let(block)
