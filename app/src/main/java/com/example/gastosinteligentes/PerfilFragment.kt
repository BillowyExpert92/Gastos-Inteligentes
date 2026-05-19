package com.example.gastosinteligentes

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gastosinteligentes.database.AppDatabase
import com.example.gastosinteligentes.database.entidades.Usuario
import com.example.gastosinteligentes.utils.SessionManager
import android.util.Patterns

class PerfilFragment : Fragment(R.layout.fragment_perfil) {

    private lateinit var db: AppDatabase
    private lateinit var session: SessionManager

    private lateinit var txtNombre: TextView
    private lateinit var txtCorreo: TextView
    private lateinit var btnEditar: Button
    private lateinit var btnCerrar: Button

    private var usuarioActual: Usuario? = null
    private var idUsuario = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getDatabase(requireContext())
        session = SessionManager(requireContext())

        idUsuario = session.obtenerUsuarioId()

        txtNombre = view.findViewById(R.id.txtNombre)
        txtCorreo = view.findViewById(R.id.txtCorreo)
        btnEditar = view.findViewById(R.id.btnEditar)
        btnCerrar = view.findViewById(R.id.btnCerrarSesion)

        cargarUsuario()

        btnEditar.setOnClickListener {
            mostrarDialogoEditarPerfil()
        }

        btnCerrar.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Sesión cerrada",
                Toast.LENGTH_SHORT
            ).show()

            val session =
                SessionManager(requireContext())

            session.cerrarSesion()

            startActivity(
                Intent(requireContext(), LoginActivity::class.java)
            )

            requireActivity().finish()
        }
    }

    private fun cargarUsuario() {
        usuarioActual = db.appDao().obtenerUsuarioPorId(idUsuario)

        if (usuarioActual != null) {
            txtNombre.text = usuarioActual!!.nombre
            txtCorreo.text = usuarioActual!!.correo
        }
    }

    private fun mostrarDialogoEditarPerfil() {

        val usuario = usuarioActual ?: return

        val contenedor =
            LinearLayout(requireContext())

        contenedor.orientation =
            LinearLayout.VERTICAL

        contenedor.setPadding(
            40,
            20,
            40,
            10
        )

        val edtNombre =
            EditText(requireContext())

        edtNombre.hint = "Nombre"

        edtNombre.setText(usuario.nombre)

        val edtCorreo =
            EditText(requireContext())

        edtCorreo.hint = "Correo"

        edtCorreo.setText(usuario.correo)

        val edtContrasena =
            EditText(requireContext())

        edtContrasena.hint = "Contraseña"

        edtContrasena.setText(usuario.contraseña)

        contenedor.addView(edtNombre)

        contenedor.addView(edtCorreo)

        contenedor.addView(edtContrasena)

        val dialog =
            AlertDialog.Builder(requireContext())

                .setTitle("Editar perfil")

                .setView(contenedor)

                .setPositiveButton(
                    "Guardar",
                    null
                )

                .setNegativeButton(
                    "Cancelar",
                    null
                )

                .create()

        dialog.setOnShowListener {

            val btnGuardar =
                dialog.getButton(
                    AlertDialog.BUTTON_POSITIVE
                )

            btnGuardar.setOnClickListener {

                val nombre =
                    edtNombre.text
                        .toString()
                        .trim()

                val correo =
                    edtCorreo.text
                        .toString()
                        .trim()

                val contrasena =
                    edtContrasena.text
                        .toString()
                        .trim()

                // =========================
                // VALIDAR NOMBRE
                // =========================

                if(nombre.isEmpty()){

                    edtNombre.error =
                        "Ingresa tu nombre"

                    return@setOnClickListener
                }

                // =========================
                // VALIDAR CORREO
                // =========================

                if(correo.isEmpty()){

                    edtCorreo.error =
                        "Ingresa tu correo"

                    return@setOnClickListener
                }

                if(
                    !Patterns.EMAIL_ADDRESS
                        .matcher(correo)
                        .matches()
                ){

                    edtCorreo.error =
                        "Correo inválido"

                    return@setOnClickListener
                }

                // =========================
                // VALIDAR CONTRASEÑA
                // =========================

                if(contrasena.isEmpty()){

                    edtContrasena.error =
                        "Ingresa tu contraseña"

                    return@setOnClickListener
                }

                if(contrasena.length < 6){

                    edtContrasena.error =
                        "Mínimo 6 caracteres"

                    return@setOnClickListener
                }

                // =========================
                // VALIDAR CORREO REPETIDO
                // =========================

                val usuarioExistente =
                    db.appDao()
                        .obtenerUsuarioPorCorreo(
                            correo
                        )

                if(
                    usuarioExistente != null &&
                    usuarioExistente.id != usuario.id
                ){

                    edtCorreo.error =
                        "Este correo ya está registrado"

                    return@setOnClickListener
                }

                // =========================
                // ACTUALIZAR
                // =========================

                usuario.nombre =
                    nombre

                usuario.correo =
                    correo

                usuario.contraseña =
                    contrasena

                db.appDao()
                    .actualizarUsuario(usuario)

                cargarUsuario()

                Toast.makeText(
                    requireContext(),
                    "Perfil actualizado",
                    Toast.LENGTH_SHORT
                ).show()


                dialog.dismiss()
            }
        }

        dialog.show()
    }
}