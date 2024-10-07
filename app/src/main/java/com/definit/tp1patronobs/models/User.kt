package com.definit.tp1patronobs.models

import java.io.Serializable

data class User(
    val username: String,
    val email: String,
    val password: String,
    val gender: String? = null,
    val userBooks: MutableList<UserBook> = mutableListOf()
) : Serializable