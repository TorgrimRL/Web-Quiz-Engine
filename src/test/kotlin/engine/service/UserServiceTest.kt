package engine.service

import engine.dto.RegistrationRequest
import engine.entity.User
import engine.repository.UserRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.OverrideMockKs
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.server.ResponseStatusException

@ExtendWith(MockKExtension::class)
class UserServiceTest {
  @MockK private lateinit var userRepository: UserRepository

  @MockK private lateinit var passwordEncoder: PasswordEncoder

  @OverrideMockKs private lateinit var userService: UserService

  @Test
  fun `registerUser should save new user with encoded password`() {
    val request = RegistrationRequest("new@email.com", "password123")
    val encodedPassword = "encodedPassword123"

    every { userRepository.existsUserByEmail(request.email) } returns false
    every { passwordEncoder.encode(request.password) } returns encodedPassword
    every { userRepository.save(any()) } answers { firstArg() }

    userService.registerUser(request)

    val userSlot = slot<User>()
    verify(exactly = 1) { userRepository.save(capture(userSlot)) }

    assertEquals(request.email, userSlot.captured.email)
    assertEquals(encodedPassword, userSlot.captured.password)
  }

  @Test
  fun `registerUser should throw BAD_REQUEST when email already exists`() {
    val request = RegistrationRequest("existing@email.com", "password123")

    every { userRepository.existsUserByEmail(request.email) } returns true

    val exception = assertThrows<ResponseStatusException> { userService.registerUser(request) }

    assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)
    verify(exactly = 0) { passwordEncoder.encode(any()) }
    verify(exactly = 0) { userRepository.save(any()) }
  }
}
