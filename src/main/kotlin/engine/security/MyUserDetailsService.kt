package engine.security

import engine.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class MyUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

  @Throws(UsernameNotFoundException::class)
  override fun loadUserByUsername(email: String) =
      userRepository.findUserByEmail(email)?.let { entity ->
        User.withUsername(entity.email)
            .password(entity.password)
            .roles("USER")
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build()
      } ?: throw UsernameNotFoundException("User $email not found")
}
