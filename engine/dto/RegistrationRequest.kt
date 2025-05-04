package engine.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class RegistrationRequest(
    @field:Email @field:Pattern(
            regexp = ".+@.+\\..+") val email: String, @field:Size(min = 5) val password: String)