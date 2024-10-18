package com.definit.tp1patronobs.home

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.definit.tp1patronobs.models.User
import com.definit.tp1patronobs.models.Book
import com.definit.tp1patronobs.models.UserBook
import kotlinx.coroutines.launch


class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user
    val userBooks = MutableLiveData<List<UserBook>>()

    private val _books = MutableLiveData<MutableList<Book>>(mutableListOf())
    val books: LiveData<MutableList<Book>> get() = _books

    // Initialize SharedPreferences
    private val sharedPreferences: SharedPreferences = application.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
    // LiveData para el tema oscuro
    private val _isDarkMode = MutableLiveData<Boolean>()
    val isDarkMode: LiveData<Boolean> get() = _isDarkMode

    // Initialize isDarkMode from SharedPreferences
    init {
        val isDarkModeEnabled = sharedPreferences.getBoolean("dark_mode", false)
        _isDarkMode.value = isDarkModeEnabled

        // Set the initial theme
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    // Función para cambiar el tema oscuro
    fun setDarkMode(isDarkMode: Boolean) {
        val currentMode = if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        if (AppCompatDelegate.getDefaultNightMode() != currentMode) {
            viewModelScope.launch {
                // Actualizar el valor en SharedPreferences
                sharedPreferences.edit().putBoolean("dark_mode", isDarkMode).apply()
                _isDarkMode.value = isDarkMode

                // Aplicar el cambio de tema
                AppCompatDelegate.setDefaultNightMode(currentMode)
            }
        }
    }

    fun setUser(user: User) {
        _user.value = user
    }

    // Agregar un libro a la lista
    fun addBook(book: Book) {
        val currentList = _books.value ?: mutableListOf()
        currentList.add(book)
        _books.value = currentList
    }

    // Obtener la lista de libros
    fun getBooks(): List<Book>? {
        return _books.value
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
