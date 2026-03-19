package com.example.gastosinteligentes

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PerfilFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PerfilFragment : Fragment(R.layout.fragment_perfil) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val nombre = view.findViewById<TextView>(R.id.txtNombre)
        val correo = view.findViewById<TextView>(R.id.txtCorreo)
        val btnCerrar = view.findViewById<Button>(R.id.btnCerrarSesion)

        // DATOS DUMMY (reemplazar con sesión real)
        nombre.text = "Juan Pérez"
        correo.text = "juan@email.com"

        btnCerrar.setOnClickListener {
            Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()

            // Redirigir al login
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }
}