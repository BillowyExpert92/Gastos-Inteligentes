package com.example.gastosinteligentes

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        loadFragment(InicioFragment())

        bottomNav.setOnItemSelectedListener {

            when(it.itemId){

                R.id.nav_inicio -> loadFragment(InicioFragment())

                R.id.nav_categorias -> loadFragment(CategoriasFragment())

                R.id.nav_estadisticas -> loadFragment(EstadisticasFragment())

                R.id.nav_perfil -> loadFragment(PerfilFragment())

            }

            true
        }
    }

    private fun loadFragment(fragment: Fragment){

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()

    }
}