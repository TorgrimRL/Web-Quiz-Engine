package engine.config

import jakarta.servlet.DispatcherType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig(private val userDetailsService: UserDetailsService) {

  @Bean
  @Throws(Exception::class)
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
    http
        .httpBasic(Customizer.withDefaults())
        .csrf { it.disable() }
        .authorizeHttpRequests { auth ->
          auth
              .requestMatchers("/actuator/health","/actuator/health/**").permitAll()
              .requestMatchers(HttpMethod.POST, "/api/register", "/actuator/shutdown")
              .permitAll()
              .dispatcherTypeMatchers(DispatcherType.ERROR)
              .permitAll()
              .anyRequest()
              .authenticated()
        }

    return http.build()
  }

  @Bean fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
