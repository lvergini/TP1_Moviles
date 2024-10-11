package com.definit.tp1patronobs.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.definit.tp1patronobs.models.User

class MainViewModel: ViewModel() {
    var mainViewState = MutableLiveData<MainStates>()
    private var username: String = ""
    private var password: String = ""
    private var usersList: List<User> = emptyList()

    fun setUsersList(users: List<User>) {
        usersList = users
    }

    fun validateUsername(username: String) {
        this.username = username
        validateButton()
    }

    fun validatePassword(password: String) {
        this.password = password
        validateButton()
    }

    private fun validateButton() {
        if (username.isNotBlank() && password.isNotBlank()) {
            mainViewState.value = MainStates.SuccessButton

        } else {
            mainViewState.value = MainStates.ErrorButton
        }
    }

    fun validateUserCredentials(): Boolean {
        val matchingUser = usersList.find { it.username == username }
        return matchingUser != null && matchingUser.password == password
    }

}