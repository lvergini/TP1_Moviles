package com.definit.tp1patronobs.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.definit.tp1patronobs.R
import com.definit.tp1patronobs.models.User
import com.definit.tp1patronobs.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var preferences: SharedPreferences
    private lateinit var gson: Gson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences( com.definit.tp1patronobs.register.RegisterActivity.CREDENTIALS, MODE_PRIVATE)
        gson = Gson()

        //Verificar si hay sesión activa
        val actualUserJson = preferences.getString("actualUser", null)
        if (actualUserJson != null) {
            val actualUser = gson.fromJson(actualUserJson, User::class.java) //Convertir JSON de actualUser en objeto User
            goToHomeActivity(actualUser) // Redirigir a HomeActivity si hay una sesión activa
            return
        }

        // Cargar la lista de usuarios en el ViewModel
        val usersList = getUsersList()
        viewModel.setUsersList(usersList)

        // Observadores de cambios en los campos
        binding.etUsername.addTextChangedListener { username ->
            viewModel.validateUsername(username = username.toString())
        }
        binding.etPassword.addTextChangedListener { password ->
            viewModel.validatePassword(password = password.toString())
        }

        viewModel.mainViewState.observe(this, Observer { state->
            when(state) {
                MainStates.ErrorButton -> {
                    binding.btnSignIn.isEnabled = false
                }
                MainStates.SuccessButton -> {
                    binding.btnSignIn.isEnabled = true
                }
            }
        })

        binding.btnSignIn.setOnClickListener {

            if (viewModel.validateUserCredentials()) {
                val matchingUser = usersList.find { it.username == binding.etUsername.text.toString() }
                if (matchingUser != null && binding.checkboxRememberMe.isChecked) {
                    saveCurrentUser(matchingUser)
                }
                goToHomeActivity(matchingUser) // Pasar el objeto User a HomeActivity
            } else {
                Toast.makeText(this,
                    getString(R.string.error_invalid_credentials), Toast.LENGTH_SHORT).show()
            }

        }
        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, com.definit.tp1patronobs.register.RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getUsersList(): List<User> {
        val usersJson = preferences.getString("users", null)
        return if (usersJson != null) {
            val type = object : TypeToken<List<User>>() {}.type
            gson.fromJson(usersJson, type)
        } else {
            emptyList()
        }
    }

    private fun goToHomeActivity(user: User?) {
        val intent = Intent(this, com.definit.tp1patronobs.home.HomeActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    // Función para guardar el objeto User en SharedPreferences como JSON
    private fun saveCurrentUser(user: User) {
        val edit = preferences.edit() // hacerlo variable global
        val userJson = gson.toJson(user)
        edit.putString("actualUser", userJson)
        edit.apply()
    }
}