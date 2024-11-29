data class LoginResponse(
    val jwt: String,
    val user: User
)

data class User(
    val id: String,
    val username: String,
    val email: String,
    val persona: Persona?
)

data class Persona(
    val id: String,
    val rol: String
)
