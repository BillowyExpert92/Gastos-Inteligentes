package com.example.gastosinteligentes

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gastosinteligentes.database.AppDatabase
import com.example.gastosinteligentes.utils.SessionManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        // BASE DE DATOS

        val db = AppDatabase.getDatabase(this)

        // SESSION MANAGER

        val session = SessionManager(this)

        // =========================
        // LAYOUTS
        // =========================

        val layoutCorreo =
            findViewById<TextInputLayout>(
                R.id.layoutCorreo
            )

        val layoutContrasena =
            findViewById<TextInputLayout>(
                R.id.layoutContrasena
            )

        // =========================
        // EDITTEXTS
        // =========================

        val editCorreo =
            findViewById<TextInputEditText>(
                R.id.editCorreo
            )

        val editContrasena =
            findViewById<TextInputEditText>(
                R.id.editContrasena
            )

        // =========================
        // BOTONES
        // =========================

        val btnLogin =
            findViewById<Button>(
                R.id.btnLogin
            )

        val txtRegistro =
            findViewById<TextView>(
                R.id.txtRegistro
            )

        // =========================
        // IR A REGISTRO
        // =========================

        txtRegistro.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RegistroActivity::class.java
                )
            )
        }

        // =========================
        // LOGIN
        // =========================

        btnLogin.setOnClickListener {

            // LIMPIAR ERRORES

            layoutCorreo.error = null
            layoutContrasena.error = null

            // OBTENER DATOS

            val correo =
                editCorreo.text
                    .toString()
                    .trim()

            val contrasena =
                editContrasena.text
                    .toString()
                    .trim()

            // =========================
            // VALIDACIONES
            // =========================

            var hayErrores = false

            // VALIDAR CORREO

            if(correo.isEmpty()){

                layoutCorreo.error =
                    "Ingresa tu correo"

                hayErrores = true

            }else if(
                !Patterns.EMAIL_ADDRESS
                    .matcher(correo)
                    .matches()
            ){

                layoutCorreo.error =
                    "Correo inválido"

                hayErrores = true
            }

            // VALIDAR CONTRASEÑA

            if(contrasena.isEmpty()){

                layoutContrasena.error =
                    "Ingresa tu contraseña"

                hayErrores = true
            }

            // DETENER SI HAY ERRORES

            if(hayErrores){
                return@setOnClickListener
            }

            // =========================
            // CONSULTAR USUARIO
            // =========================

            val usuario =
                db.appDao().login(
                    correo,
                    contrasena
                )

            // =========================
            // VALIDAR LOGIN
            // =========================

            if(usuario == null){

                Toast.makeText(
                    this,
                    "Correo o contraseña incorrectos",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // =========================
            // GUARDAR SESION
            // =========================

            session.guardarUsuario(
                usuario.id
            )

            // =========================
            // LOGIN EXITOSO
            // =========================

            Toast.makeText(
                this,
                "Bienvenido ${usuario.nombre}",
                Toast.LENGTH_SHORT
            ).show()

            // =========================
            // IR AL MAIN
            // =========================

            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )

            finish()
        }
    }
}