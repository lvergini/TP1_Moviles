package com.definit.tp1patronobs.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
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

        val actualUserJson = preferences.getString("actualUser", null)
        if (actualUserJson != null) {
            //Convertir JSON de actualUser en objeto User
            val actualUser = gson.fromJson(actualUserJson, User::class.java)
            goToHomeActivity(actualUser) // Redirigir a HomeActivity si hay una sesión activa
            return
        }

        binding.etUsername.addTextChangedListener { username ->
            viewModel.validateUsername(username = username.toString())
        }
        binding.etPassword.addTextChangedListener { password ->
            viewModel.validatePassword(password = password.toString())
        }

        viewModel.mainViewState.observe(this, Observer { state->
            when(state) {
                MainStates.ErrorButton -> {
                    binding.btnSingIn.isEnabled = false
                }
                MainStates.SuccessButton -> {
                    binding.btnSingIn.isEnabled = true
                }
                /*MainStates.ErrorUsername -> {
                    binding.layoutUsername.error = "Correo inválido"
                }
                MainStates.ErrorPassword -> binding.layoutPassword.error = "Contraseña inválida"
                MainStates.SuccessButton -> {
                    binding.btnSingIn.isEnabled = true
                }
                MainStates.SuccessPassword -> {
                    binding.layoutPassword.error = null
                }
                MainStates.SuccessUsername -> {
                    binding.layoutUsername.error = null
                }*/

            }
        })

        binding.btnSingIn.setOnClickListener {
            val user = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            if (validateData(user, password)) {
                val matchingUser = getUsersList().find { it.username == user }
                if (binding.checkbox.isChecked && matchingUser != null) {
                    saveCurrentUser(matchingUser)
                }
                goToHomeActivity(matchingUser) // Pasar el objeto User a HomeActivity
            }

        }
        binding.btnSingUp.setOnClickListener {
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
    private fun validateData(user: String, password: String): Boolean {
        val usersList = getUsersList()

        //Buscar al usuario en la lista
        val matchingUser = usersList.find { it.username == user }

        // Validar si el usuario existe y la contraseña es correcta
        if (matchingUser != null && matchingUser.password == password) {
            return true
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            return false
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