package engine.repository

import engine.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Int> {
  fun findUserByEmail(email: String): User?

  fun existsUserByEmail(email: String): Boolean
}
