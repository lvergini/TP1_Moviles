package com.definit.tp1patronobs.register


sealed class RegisterStates {
    object ErrorEmail : RegisterStates()

    data class ErrorPassword(val errorTypes: List<PasswordError>) : RegisterStates() {
        enum class PasswordError {
            LENGTH, LETTER, NUMBER
        }
    }
    object ErrorConfirmPassword : RegisterStates()
    object ErrorUsernameTaken : RegisterStates()
    object ErrorUsernameBlank: RegisterStates()
    object FormValid : RegisterStates()
    object FormInvalid: RegisterStates()
    object SuccessUsername: RegisterStates()
    object SuccessEmail: RegisterStates()
    object SuccessPassword: RegisterStates()
    object SuccessConfirmPassword: RegisterStates()
}