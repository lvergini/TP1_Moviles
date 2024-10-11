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
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import com.definit.tp1patronobs.R
import com.definit.tp1patronobs.models.User
import com.definit.tp1patronobs.databinding.ActivityHomeBinding
import com.definit.tp1patronobs.home.fragments.SettingsFragment
import com.definit.tp1patronobs.home.fragments.HomeFragment
import com.definit.tp1patronobs.home.fragments.MyBooksFragment
import com.definit.tp1patronobs.home.fragments.SearchFragment
import com.definit.tp1patronobs.main.MainActivity
import com.definit.tp1patronobs.register.RegisterActivity
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

        // Obtener el usuario pasado desde MainActivity
        val user = intent.getSerializableExtra("user") as? User
        user?.let {
            // Pasar el usuario al ViewModel compartido
            sharedViewModel.setUser(it)
        }

        // Obtener el header de NavigationView
        //val headerView = binding.navigationView.getHeaderView(0)
        //val navHeaderUsername = headerView.findViewById<TextView>(R.id.tvNavHeader)

        // Mostrar el nombre de usuario en el TextView
        /*user?.let {
            navHeaderUsername.text = it.username
        }*/

        // Observar los cambios del usuario en el ViewModel y mostrar en el header
        sharedViewModel.user.observe(this, Observer { user ->
            val headerView = binding.navigationView.getHeaderView(0)
            val navHeaderUsername = headerView.findViewById<TextView>(R.id.tvNavHeader)
            navHeaderUsername.text = user.username
        })

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        drawerLayout = binding.drawerLayout

        //Para manejar interacción (cuándo abrir y cuándo cerrar)
         val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_home_open, R.string.nav_drawer_home_close)
        drawerLayout.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar?.title = getString(R.string.txt_app_name)

        //implementación de interfaz para saber dónde hicimos click
        binding.navigationView.setNavigationItemSelectedListener(this)

        //mostrar fragmento al inicio
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
            binding.navigationView.setCheckedItem(R.id.nav_home)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_home -> {
                replaceFragment(HomeFragment())
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_my_books -> {
                replaceFragment(MyBooksFragment())
                Toast.makeText(this,"My books",Toast.LENGTH_SHORT).show()
            }
            R.id.nav_search -> {
                replaceFragment(SearchFragment())
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_settings -> {
                replaceFragment(SettingsFragment())
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_logout -> {
                goToMainActivity()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }

    private fun goToMainActivity() {
        // Borrar el actualUser de SharedPreferences
        val editor = getSharedPreferences(RegisterActivity.CREDENTIALS, MODE_PRIVATE).edit()
        editor.remove("actualUser")
        editor.apply() // Aplicar los cambios

        // Redirigir a MainActivity

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