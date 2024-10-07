package com.definit.tp1patronobs.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    var mainViewState = MutableLiveData<MainStates>()
    private var username: String = ""
    private var password: String = ""

    fun validateUsername(username: String) {
        this.username = username

        /*if (username.isNotBlank()) {
            mainViewState.value = MainStates.SuccessUsername
        } else {
            mainViewState.value = MainStates.ErrorUsername
        } */
        validateButton()
    }

    fun validatePassword(password: String) {
        this.password = password
        /*if (password.isNotBlank()) {
            mainViewState.value = MainStates.SuccessPassword

        } else {
            mainViewState.value = MainStates.ErrorPassword
        }*/
        validateButton()
    }

    private fun validateButton() {
        if (username.isNotBlank() && password.isNotBlank()) {
            mainViewState.value = MainStates.SuccessButton

        } else {
            mainViewState.value = MainStates.ErrorButton
        }
    }

}