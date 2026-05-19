package com.example.gastosinteligentes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import android.content.Intent
import com.example.gastosinteligentes.utils.SessionManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val session =
            SessionManager(this)

        if (session.obtenerUsuarioId() == -1) {

            startActivity(
                Intent(this, LoginActivity::class.java)
            )

            finish()

            return
        }

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