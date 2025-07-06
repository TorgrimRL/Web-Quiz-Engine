package engine.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int? = null,
    val email: String,
    val password: String,
    @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.EAGER)
    val quizzes: MutableList<Quiz> = mutableListOf(),
    @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.EAGER)
    val quizCompletions: MutableList<QuizCompletion> = mutableListOf()
)
