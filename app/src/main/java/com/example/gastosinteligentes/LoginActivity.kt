package com.example.gastosinteligentes

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button
import android.widget.TextView

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val registro = findViewById<TextView>(R.id.txtRegistro)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        registro.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        // Botón LOGIN → MainActivity
        btnLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish() // 🔥 evita regresar
        }
    }
}