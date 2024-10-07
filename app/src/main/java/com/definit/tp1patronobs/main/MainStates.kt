package com.definit.tp1patronobs.main


sealed class MainStates {

    object SuccessButton: MainStates()
    object ErrorButton: MainStates()

}