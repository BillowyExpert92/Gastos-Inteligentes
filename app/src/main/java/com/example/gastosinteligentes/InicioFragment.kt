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
class InicioFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: GastoAdaptador

    private var listaGastos = mutableListOf<Gasto>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_inicio, container, false)

        recycler = view.findViewById(R.id.listaGastos)

        adapter = GastoAdaptador(listaGastos)

        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter

        val btnAgregar = view.findViewById<FloatingActionButton>(R.id.btnAgregar)

        btnAgregar.setOnClickListener {

            mostrarDialogo()

        }

        return view
    }

    private fun mostrarDialogo(){

        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_agregar_gasto,null)

        val edtMonto = dialogView.findViewById<EditText>(R.id.edtMonto)
        val edtDescripcion = dialogView.findViewById<EditText>(R.id.edtDescripcion)
        val spinner = dialogView.findViewById<Spinner>(R.id.spinnerCategoria)
        val edtFecha = dialogView.findViewById<EditText>(R.id.edtFecha)

        val categorias = listOf(
            "Transporte",
            "Comida",
            "Entretenimiento",
            "Escuela",
            "Servicios",
            "Mercado",
            "Otro"
        )

        spinner.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            categorias)

        val hoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        edtFecha.setText(hoy)

        AlertDialog.Builder(requireContext())
            .setTitle("Agregar gasto")
            .setView(dialogView)
            .setPositiveButton("Agregar"){_,_->

                val gasto = Gasto(

                    edtDescripcion.text.toString(),
                    edtMonto.text.toString().toDouble(),
                    spinner.selectedItem.toString(),
                    edtFecha.text.toString()

                )

                listaGastos.add(0,gasto)

                adapter.notifyDataSetChanged()

            }
            .show()

    }
}