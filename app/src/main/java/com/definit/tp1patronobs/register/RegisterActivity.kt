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
import com.definit.tp1patronobs.repository.UserRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var preferences: SharedPreferences
    private lateinit var gson: Gson
    private lateinit var userRepository: UserRepository

    val arrayGenders: Array<Genders> = Genders.values()
    var genderSelected: Genders? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar SharedPreferences y GSON
        preferences = getSharedPreferences(CREDENTIALS, MODE_PRIVATE)
        gson = Gson()
        userRepository = UserRepository(preferences, gson)

        val usersList = userRepository.getUsersList() // Obtener la lista de usuarios desde SharedPreferences

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
            viewModel.validatePassword(password.toString())
        }
        binding.etConfirmPassword.addTextChangedListener { confirmPassword ->
            viewModel.validateConfirmPassword(confirmPassword.toString())
        }

        // Observar los cambios de estado en viewModel
        viewModel.viewState.observe(this) {state->
            when(state) {
                is RegisterStates.ErrorEmail -> {
                    binding.layoutEmail.error = getString(R.string.error_email_invalid)
                }
                is RegisterStates.SuccessEmail -> {
                    binding.layoutEmail.error = null
                }
                is RegisterStates.ErrorPassword -> {

                    val errorMessages = state.errorTypes.map { errorType ->
                        when (errorType) {
                            RegisterStates.ErrorPassword.PasswordError.LENGTH -> getString(R.string.error_password_length)
                            RegisterStates.ErrorPassword.PasswordError.LETTER -> getString(R.string.error_password_letter)
                            RegisterStates.ErrorPassword.PasswordError.NUMBER -> getString(R.string.error_password_number)
                        }
                    }
                    binding.layoutPassword.error = getString(R.string.error_password) + errorMessages.joinToString(", ")

                }
                is RegisterStates.SuccessPassword -> {
                    binding.layoutPassword.error = null
                }
                is RegisterStates.ErrorConfirmPassword -> {
                    binding.layoutConfirmPassword.error = getString(R.string.error_confirm_password)
                }
                is RegisterStates.SuccessConfirmPassword -> {
                    binding.layoutConfirmPassword.error = null
                }
                is RegisterStates.ErrorUsernameTaken -> {
                    binding.layoutUsername.error = getString(R.string.error_username_taken)
                }
                RegisterStates.ErrorUsernameBlank -> {
                    binding.layoutUsername.error = getString(R.string.error_username_blank)
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

        val newUser = User(username, email, password, gender)

        // Usar el repositorio para agregar el nuevo usuario
        userRepository.addUser(newUser)

        Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
        goToMainActivity()
    }

    private fun goToMainActivity() {
        val intent = Intent(this, com.definit.tp1patronobs.main.MainActivity::class.java)
        startActivity(intent)
    }


    companion object {
        const val CREDENTIALS = "Credenciales"
    }
}