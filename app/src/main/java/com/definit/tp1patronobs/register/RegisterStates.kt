package com.definit.tp1patronobs.register


sealed class RegisterStates {
    object ErrorEmail : RegisterStates()
    data class ErrorPassword(val message: String): RegisterStates()
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