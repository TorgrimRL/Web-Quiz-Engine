package engine.dto

import jakarta.validation.constraints.NotNull

data class QuizAnswerRequest(@NotNull val answer: List<Int>?)
