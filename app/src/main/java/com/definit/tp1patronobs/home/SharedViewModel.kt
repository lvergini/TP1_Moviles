package com.definit.tp1patronobs.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.definit.tp1patronobs.models.User
import com.definit.tp1patronobs.models.Book
import com.definit.tp1patronobs.models.UserBook

class SharedViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user
    val userBooks = MutableLiveData<List<UserBook>>()

    fun setUser(user: User) {
        _user.value = user
    }

    // Obtener libros del usuario
    fun getUserBooks(): List<UserBook>? {
        return _user.value?.userBooks
    }

    // Agregar un libro
    fun addBookToUser(book: UserBook) {
        _user.value?.userBooks?.add(book)
    }

    fun markBookAsRead(book: UserBook) {
        val userBooks = _user.value?.userBooks
        val bookToMark = userBooks?.find { it.book.id == book.book.id }
        bookToMark?.isRead = true
    }
    // Función para agregar o remover un libro
    fun toggleBookAdded(book: Book) {
        val currentList = userBooks.value?.toMutableList() ?: mutableListOf()
        val userBook = currentList.find { it.book.id == book.id }

        if (userBook != null) {
            // Cambiar el estado de agregado
            userBook.isAdded = !userBook.isAdded
        } else {
            // Agregar un nuevo UserBook si no existe
            currentList.add(UserBook(book = book, isAdded = true))
        }

        userBooks.value = currentList // Notificar los cambios
    }

    // Función para verificar si un libro está agregado
    fun isBookAdded(book: Book): Boolean {
        return userBooks.value?.any { it.book.id == book.id && it.isAdded } ?: false
    }
}