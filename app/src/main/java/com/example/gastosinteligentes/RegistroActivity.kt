package com.example.gastosinteligentes

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button
import android.widget.TextView

class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val login = findViewById<TextView>(R.id.txtLogin)
        val btnRegistro = findViewById<Button>(R.id.btnRegistro)

        login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // Botón REGISTRO → MainActivity
        btnRegistro.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish() // 🔥 evita regresar al registro
        }
    }
}