package com.definit.tp1patronobs.repository

import android.content.SharedPreferences
import com.definit.tp1patronobs.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserRepository(private val preferences: SharedPreferences, private val gson: Gson) {

    //Obtener lista de usuarios desde Shared Preferences
    fun getUsersList(): List<User> {
        val usersJson = preferences.getString("users", null)
        return if (usersJson != null) {
            val type = object : TypeToken<List<User>>() {}.type
            gson.fromJson(usersJson, type)
        } else {
            emptyList()
        }
    }

    // Guardar lista de usuarios en Shared Preferences
    fun saveUsersList(usersList: List<User>) {
        val usersJson = gson.toJson(usersList)
        val edit = preferences.edit()
        edit.putString("users", usersJson)
        edit.apply()
    }

    //Agregar un nuevo usuario
    fun addUser(newUser: User) {
        val usersList = getUsersList().toMutableList()
        usersList.add(newUser)
        saveUsersList(usersList)
    }

    // Mantener sesión iniciada (guardar usuario actual)
    fun saveCurrentUser(user: User) {
        val userJson = gson.toJson(user)
        val edit = preferences.edit()
        edit.putString("actualUser", userJson)
        edit.apply()
    }

    // Obtener usuario actual (sesión activa)
    fun getCurrentUser(): User? {
        val userJson = preferences.getString("actualUser", null)
        return if (userJson != null) {
            gson.fromJson(userJson, User::class.java)
        } else {
            null
        }
    }

    // Cerrar sesión del usuario actual
    fun logoutUser() {
        val edit = preferences.edit()
        edit.remove("actualUser")
        edit.apply()
    }

}