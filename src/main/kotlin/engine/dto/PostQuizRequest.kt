package engine.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class PostQuizRequest(
    @NotBlank val title: String?,
    @NotBlank val text: String?,
    @field:Size(min = 2) @NotNull val options: List<String>,
    val answer: List<Int>?
)
