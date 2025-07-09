package engine.service

import engine.dto.RegistrationRequest
import engine.entity.User
import engine.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

  fun registerUser(request: RegistrationRequest) {
    if (userRepository.existsUserByEmail(request.email)) {
      throw ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Mail already registered",
      )
    }
    val hashedPassword = passwordEncoder.encode(request.password)
    userRepository.save(
        User(
            email = request.email,
            password = hashedPassword,
        ),
    )
  }
}
