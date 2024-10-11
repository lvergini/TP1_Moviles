package com.definit.tp1patronobs.register

import androidx.core.util.PatternsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.definit.tp1patronobs.models.User

class RegisterViewModel:ViewModel() {
    var viewState = MutableLiveData<RegisterStates>()
    private var username = ""
    private var email = ""
    private var password = ""
    private var confirmPassword = ""

    private var isUsernameValid = false
    private var isEmailValid = false
    private var isPasswordValid = false
    private var isConfirmPasswordValid = false

    fun validateUsername(username: String, usersList: List<User>) {
        this.username = username

        if (username.isNotBlank()) {
            if (usersList.any { it.username == username }) {
                viewState.value = RegisterStates.ErrorUsernameTaken
                isUsernameValid = false
            } else {
                viewState.value = RegisterStates.SuccessUsername
                isUsernameValid = true
            }
        } else {
            viewState.value = RegisterStates.ErrorUsernameBlank
            isUsernameValid = false
        }
        validateForm()

    }

    fun validateEmail(email: String) {
        this.email = email
        if (PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            viewState.value = RegisterStates.SuccessEmail
            isEmailValid = true
        } else {
            viewState.value = RegisterStates.ErrorEmail
            isEmailValid = false
        }
        validateForm()
    }

    fun validatePassword(password: String) {
        this.password = password
        val errors = mutableListOf<String>()

        if (password.length < 8) {
            errors.add("8 caracteres")
        }
        if (!password.any { it.isLetter() }) {
            errors.add("una letra")
        }
        if (!password.any { it.isDigit() }) {
            errors.add("un número")
        }
        if (errors.isEmpty()) {
            viewState.value = RegisterStates.SuccessPassword
            isPasswordValid = true

        } else {
            val message = "Mínimo: ${errors.joinToString(", ")}"
            viewState.value = RegisterStates.ErrorPassword(message)
            isPasswordValid = false
        }
        validateConfirmPassword(confirmPassword)
        validateForm()
    }

    fun validateConfirmPassword(confirmPassword: String) {
        this.confirmPassword = confirmPassword
        if (confirmPassword == password) {
            viewState.value = RegisterStates.SuccessConfirmPassword
            isConfirmPasswordValid = true
        } else {
            viewState.value = RegisterStates.ErrorConfirmPassword
            isConfirmPasswordValid = false
        }
        validateForm()

    }

    private fun validateForm() {
        if (isUsernameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid) {
            viewState.value = RegisterStates.FormValid
        } else {
            viewState.value = RegisterStates.FormInvalid
        }
    }


}