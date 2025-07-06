package engine.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.time.OffsetTime

@Entity
data class QuizCompletion(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @JsonIgnore val id: Long? = null,
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "quiz_id")
    @JsonIgnore
    val quiz: Quiz,
    val completedAt: OffsetTime = OffsetTime.now(),
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    var user: User? = null
) {
  @get:JsonProperty("id")
  val quizId: Int?
    get() = quiz.id
}
