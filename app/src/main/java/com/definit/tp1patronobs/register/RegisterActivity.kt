package com.definit.tp1patronobs.register


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.definit.tp1patronobs.models.User  // Importar la clase User
import com.definit.tp1patronobs.Genders
import com.definit.tp1patronobs.R
import com.definit.tp1patronobs.databinding.ActivityRegisterBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var preferences: SharedPreferences
    private lateinit var gson: Gson

    val arrayGenders: Array<Genders> = Genders.values()
    var genderSelected: Genders? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar SharedPreferences y GSON
        preferences = getSharedPreferences(CREDENTIALS, MODE_PRIVATE)
        gson = Gson()

        val usersList = getUsersList() // Obtener la lista de usuarios desde SharedPreferences

        // Configuración del Spinner para el género
        val adapter = ArrayAdapter(this, R.layout.spinner_item, arrayGenders)
        binding.spinnerGender.adapter = adapter

        binding.spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                genderSelected = arrayGenders[position]
                if (genderSelected == Genders.UNKNOWN) {
                    genderSelected = null
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                genderSelected = null
            }
        }


        // Añadir TextWatcher a los campos para validar
        binding.etUsername.addTextChangedListener { username->
            viewModel.validateUsername(username.toString(), usersList)
        }
        binding.etEmail.addTextChangedListener { email ->
            viewModel.validateEmail(email.toString())
        }
        binding.etPassword.addTextChangedListener { password ->
            viewModel.validatePassword(password.toString(), this)
        }
        binding.etConfirmPassword.addTextChangedListener { confirmPassword ->
            viewModel.validateConfirmPassword(confirmPassword.toString())
        }

        // Observar los cambios de estado en viewModel
        viewModel.viewState.observe(this) {state->
            when(state) {
                is RegisterStates.ErrorEmail -> {
                    binding.layoutEmail.error = "Correo inválido"
                }
                is RegisterStates.SuccessEmail -> {
                    binding.layoutEmail.error = null
                }
                is RegisterStates.ErrorPassword -> {
                    binding.layoutPassword.error = state.message
                }
                is RegisterStates.SuccessPassword -> {
                    binding.layoutPassword.error = null
                }
                is RegisterStates.ErrorConfirmPassword -> {
                    binding.layoutConfirmPassword.error = "Las contraseñas no coinciden"
                }
                is RegisterStates.SuccessConfirmPassword -> {
                    binding.layoutConfirmPassword.error = null
                }
                is RegisterStates.ErrorUsernameTaken -> {
                    binding.layoutUsername.error = "El nombre de usuario ya está registrado"
                    //Toast.makeText(this, "El nombre de usuario ya está registrado", Toast.LENGTH_SHORT).show()
                }
                RegisterStates.ErrorUsernameBlank -> {
                    binding.layoutUsername.error = "Campo obligatorio"
                    //Toast.makeText(this, "El nombre de usuario ya está registrado", Toast.LENGTH_SHORT).show()
                }
                is RegisterStates.SuccessUsername -> {
                    binding.layoutUsername.error = null
                }
                is RegisterStates.FormValid -> {
                    binding.btnSignUp.isEnabled = true
                }
                is RegisterStates.FormInvalid -> {
                    binding.btnSignUp.isEnabled = false
                }

            }
        }


        binding.btnSignUp.setOnClickListener {
            saveUserData()
        }
        binding.btnCancel.setOnClickListener {
            goToMainActivity()
        }
    }

    private fun saveUserData() {
        val username = binding.etUsername.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val gender = genderSelected?.toString() ?: "No seleccionado"

        val newUser = User(username, email, password, gender) //gender

        // Obtener la lista de usuarios y agregar el nuevo usuario
        val usersList = getUsersList().toMutableList()
        usersList.add(newUser)

        // Guardar la lista actualizada en SharedPreferences
        val edit = preferences.edit()
        val usersJson = gson.toJson(usersList)
        edit.putString("users", usersJson)
        edit.apply()

        Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
        goToMainActivity()
    }

    private fun goToMainActivity() {
        val intent = Intent(this, com.definit.tp1patronobs.main.MainActivity::class.java)
        startActivity(intent)
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

    companion object {
        const val CREDENTIALS = "Credenciales"
    }
}