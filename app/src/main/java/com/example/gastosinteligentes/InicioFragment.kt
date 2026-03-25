package com.example.gastosinteligentes

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InicioFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InicioFragment : Fragment(R.layout.fragment_inicio) {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: GastoAdaptador

    private var listaGastos = mutableListOf<Gasto>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = view.findViewById(R.id.listaGastos)

        adapter = GastoAdaptador(listaGastos)

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        val btnAgregar = view.findViewById<FloatingActionButton>(R.id.btnAgregar)

        btnAgregar.setOnClickListener {
            mostrarDialogo()
        }
    }

    private fun mostrarDialogo(){

        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_agregar_gasto,null)

        val edtMonto = dialogView.findViewById<EditText>(R.id.edtMonto)
        val edtDescripcion = dialogView.findViewById<EditText>(R.id.edtDescripcion)
        val spinner = dialogView.findViewById<Spinner>(R.id.spinnerCategoria)
        val edtFecha = dialogView.findViewById<EditText>(R.id.edtFecha)
        val btnGuardar = dialogView.findViewById<View>(R.id.btnGuardar)

        val categorias = listOf(
            "Transporte",
            "Comida",
            "Entretenimiento",
            "Escuela",
            "Servicios",
            "Mercado",
            "Otro"
        )

        spinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            categorias
        )

        val hoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        edtFecha.setText(hoy)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialog.show()

        btnGuardar.setOnClickListener {

            val monto = edtMonto.text.toString().toDoubleOrNull()

            if (monto == null) {
                edtMonto.error = "Ingresa un monto válido"
                return@setOnClickListener
            }

            if (edtDescripcion.text.toString().isEmpty()) {
                edtDescripcion.error = "Ingresa una descripción"
                return@setOnClickListener
            }

            val gasto = Gasto(
                edtDescripcion.text.toString(),
                monto,
                spinner.selectedItem.toString(),
                edtFecha.text.toString()
            )

            listaGastos.add(0, gasto)
            adapter.notifyItemInserted(0)

            recycler.scrollToPosition(0)

            dialog.dismiss()
        }
    }
}