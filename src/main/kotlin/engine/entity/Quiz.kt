package engine.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode

@Entity
@Table(name = "quiz")
data class Quiz(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int? = null,
    val title: String,
    val text: String,
    @ElementCollection(fetch = FetchType.EAGER) val options: List<String>,
    @Fetch(value = FetchMode.SUBSELECT)
    @ElementCollection(fetch = FetchType.EAGER)
    @JsonIgnore
    val answer: List<Int>,
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    var user: User? = null,
    @OneToMany(
        mappedBy = "quiz",
    )
    @JsonIgnore
    val completions: MutableList<QuizCompletion> = mutableListOf()
)
