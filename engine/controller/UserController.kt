package engine.controller

import engine.service.UserService
import engine.dto.RegistrationRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
class UserController(
    private val userService: UserService,
                    ) {
    @PostMapping("/api/register")
    fun register(@RequestBody @Valid request: RegistrationRequest): ResponseEntity<Void> {
        userService.registerUser(request)
        return ResponseEntity.ok().build()
    }
}