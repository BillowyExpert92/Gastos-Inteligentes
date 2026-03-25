package com.example.gastosinteligentes

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gastosinteligentes.database.AppDatabase
import com.example.gastosinteligentes.database.entidades.Usuario
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // =========================
        // BASE DE DATOS
        // =========================

        val db = AppDatabase.getDatabase(this)

        // =========================
        // LAYOUTS
        // =========================

        val layoutNombre =
            findViewById<TextInputLayout>(R.id.layoutNombre)

        val layoutCorreo =
            findViewById<TextInputLayout>(R.id.layoutCorreo)

        val layoutContrasena =
            findViewById<TextInputLayout>(R.id.layoutContrasena)

        val layoutConfirmarContrasena =
            findViewById<TextInputLayout>(R.id.layoutConfirmarContrasena)

        // =========================
        // EDITTEXTS
        // =========================

        val editNombre =
            findViewById<TextInputEditText>(R.id.editNombre)

        val editCorreo =
            findViewById<TextInputEditText>(R.id.editCorreo)

        val editContrasena =
            findViewById<TextInputEditText>(R.id.editContrasena)

        val editConfirmarContrasena =
            findViewById<TextInputEditText>(R.id.editConfirmarContrasena)

        // =========================
        // BOTONES
        // =========================

        val btnRegistro =
            findViewById<Button>(R.id.btnRegistro)

        val txtLogin =
            findViewById<TextView>(R.id.txtLogin)

        // =========================
        // IR A LOGIN
        // =========================

        txtLogin.setOnClickListener {

            startActivity(
                Intent(this, LoginActivity::class.java)
            )
        }

        // =========================
        // REGISTRO
        // =========================

        btnRegistro.setOnClickListener {

            // LIMPIAR ERRORES
            layoutNombre.error = null
            layoutCorreo.error = null
            layoutContrasena.error = null
            layoutConfirmarContrasena.error = null

            // OBTENER DATOS
            val nombre =
                editNombre.text.toString().trim()

            val correo =
                editCorreo.text.toString().trim()

            val contrasena =
                editContrasena.text.toString().trim()

            val confirmarContrasena =
                editConfirmarContrasena.text.toString().trim()

            // =========================
            // VALIDACIONES
            // =========================

            var hayErrores = false

            // NOMBRE
            if(nombre.isEmpty()){

                layoutNombre.error =
                    "Ingresa tu nombre"

                hayErrores = true
            }

            // CORREO
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

            // CONTRASEÑA
            if(contrasena.isEmpty()){

                layoutContrasena.error =
                    "Ingresa una contraseña"

                hayErrores = true

            }else if(contrasena.length < 6){

                layoutContrasena.error =
                    "Mínimo 6 caracteres"

                hayErrores = true
            }

            // CONFIRMAR CONTRASEÑA
            if(confirmarContrasena.isEmpty()){

                layoutConfirmarContrasena.error =
                    "Confirma tu contraseña"

                hayErrores = true

            }else if(
                contrasena != confirmarContrasena
            ){

                layoutConfirmarContrasena.error =
                    "Las contraseñas no coinciden"

                hayErrores = true
            }

            // DETENER SI HAY ERRORES
            if(hayErrores){
                return@setOnClickListener
            }

            // =========================
            // VERIFICAR USUARIO
            // =========================

            val usuarioExistente =
                db.appDao()
                    .obtenerUsuarioPorCorreo(correo)

            if(usuarioExistente != null){

                layoutCorreo.error =
                    "Este correo ya está registrado"

                return@setOnClickListener
            }

            // =========================
            // CREAR USUARIO
            // =========================

            val usuario = Usuario().apply {

                this.nombre = nombre
                this.correo = correo
                this.contraseña = contrasena
            }

            // =========================
            // GUARDAR EN BD
            // =========================

            db.appDao().insertarUsuario(usuario)

            // =========================
            // MENSAJE
            // =========================

            Toast.makeText(
                this,
                "Registro exitoso",
                Toast.LENGTH_SHORT
            ).show()

            // =========================
            // IR AL MAIN
            // =========================

            startActivity(
                Intent(this, MainActivity::class.java)
            )

            finish()
        }
    }
}