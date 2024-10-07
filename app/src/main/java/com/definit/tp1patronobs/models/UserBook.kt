package com.definit.tp1patronobs.models

data class UserBook(
    val book: Book,
    var isAdded: Boolean = false,
    var isRead: Boolean = false,
    var dateRead: String? = null
)
