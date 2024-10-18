package com.definit.tp1patronobs.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.definit.tp1patronobs.R
import com.definit.tp1patronobs.databinding.ActivityHomeBinding
import com.definit.tp1patronobs.home.fragments.HomeFragment
import com.definit.tp1patronobs.home.fragments.MyBooksFragment
import com.definit.tp1patronobs.home.fragments.ProfileFragment
import com.definit.tp1patronobs.home.fragments.SearchFragment
import com.definit.tp1patronobs.home.fragments.SettingsFragment
import com.definit.tp1patronobs.main.MainActivity
import com.definit.tp1patronobs.models.User
import com.definit.tp1patronobs.register.RegisterActivity
import com.definit.tp1patronobs.repository.UserRepository
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var drawerLayout: DrawerLayout

    // Instancia del ViewModel compartido
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupToolbarAndDrawer()
        setupNavigationView()

        // Obtener el usuario pasado desde MainActivity
        val user = intent.getSerializableExtra("user") as? User
        user?.let {
            sharedViewModel.setUser(it)
        }

        // Mostrar el fragmento inicial al cargar
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
            binding.navigationView.setCheckedItem(R.id.nav_home)
        }
    }

    private fun setupToolbarAndDrawer() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        drawerLayout = binding.drawerLayout

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.nav_drawer_home_open, R.string.nav_drawer_home_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar?.title = getString(R.string.txt_app_name)
    }

    private fun setupNavigationView() {
        binding.navigationView.setNavigationItemSelectedListener(this)
    }

    private fun setupObservers() {
        // Observar los cambios del usuario en el ViewModel y mostrar en el header
        sharedViewModel.user.observe(this) { user ->
            val headerView = binding.navigationView.getHeaderView(0)
            val navHeaderUsername = headerView.findViewById<TextView>(R.id.tvNavHeader)
            navHeaderUsername.text = user.username
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> replaceFragment(HomeFragment())
            R.id.nav_my_books -> replaceFragment(MyBooksFragment())
            R.id.nav_search -> replaceFragment(SearchFragment())
            R.id.nav_settings -> replaceFragment(SettingsFragment())
            R.id.nav_profile -> replaceFragment((ProfileFragment()))
            R.id.nav_logout -> logout()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun logout() {
        val preferences = getSharedPreferences(RegisterActivity.CREDENTIALS, MODE_PRIVATE)
        val gson = com.google.gson.Gson()
        val userRepository = UserRepository(preferences, gson)

        // Borrar usuario actual de SharedPreferences
        userRepository.logoutUser()
        Toast.makeText(this, getString(R.string.logout_success), Toast.LENGTH_SHORT).show()
        goToMainActivity()
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finalizar HomeActivity para evitar que el usuario regrese
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
