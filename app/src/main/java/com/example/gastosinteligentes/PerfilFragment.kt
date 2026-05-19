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

        val contenedor = LinearLayout(requireContext())
        contenedor.orientation = LinearLayout.VERTICAL
        contenedor.setPadding(40, 20, 40, 10)

        val edtNombre = EditText(requireContext())
        edtNombre.hint = "Nombre"
        edtNombre.setText(usuario.nombre)

        val edtCorreo = EditText(requireContext())
        edtCorreo.hint = "Correo"
        edtCorreo.setText(usuario.correo)

        val edtContrasena = EditText(requireContext())
        edtContrasena.hint = "Contraseña"
        edtContrasena.setText(usuario.contraseña)

        contenedor.addView(edtNombre)
        contenedor.addView(edtCorreo)
        contenedor.addView(edtContrasena)

        AlertDialog.Builder(requireContext())
            .setTitle("Editar perfil")
            .setView(contenedor)
            .setPositiveButton("Guardar") { _, _ ->

                val nombre = edtNombre.text.toString().trim()
                val correo = edtCorreo.text.toString().trim()
                val contrasena = edtContrasena.text.toString().trim()

                if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Completa todos los campos",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setPositiveButton
                }

                usuario.nombre = nombre
                usuario.correo = correo
                usuario.contraseña = contrasena

                db.appDao().actualizarUsuario(usuario)

                cargarUsuario()

                Toast.makeText(
                    requireContext(),
                    "Perfil actualizado",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}