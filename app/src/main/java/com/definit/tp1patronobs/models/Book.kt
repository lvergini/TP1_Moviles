package com.definit.tp1patronobs.models

data class Book(
    val id: Int, // Identificador único
    val title: String,
    val author: String,
    val genre: String,
    val coverUrl: String
)
