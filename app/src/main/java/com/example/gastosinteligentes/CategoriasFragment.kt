package com.example.gastosinteligentes

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CategoriasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CategoriasFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: CategoriaAdapter
    private val listaCategorias = mutableListOf<Categoria>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_categorias, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recycler = view.findViewById(R.id.recyclerCategorias)
        val btnAgregar = view.findViewById<FloatingActionButton>(R.id.btnAgregarCategoria)

        cargarCategoriasPorDefecto()

        adapter = CategoriaAdapter(listaCategorias)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter

        btnAgregar.setOnClickListener {
            mostrarDialogoAgregar()
        }
    }

    private fun cargarCategoriasPorDefecto() {
        listaCategorias.add(Categoria("Comida", "Gastos de comida", Color.RED))
        listaCategorias.add(Categoria("Transporte", "Pasajes y gasolina", Color.BLUE))
        listaCategorias.add(Categoria("Mercado", "Supermercado", Color.GREEN))
        listaCategorias.add(Categoria("Hogar", "Servicios y casa", Color.YELLOW))
        listaCategorias.add(Categoria("Escuela", "Gastos escolares", Color.CYAN))
    }

    private fun mostrarDialogoAgregar() {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_categoria, null)

        val nombre = dialogView.findViewById<EditText>(R.id.etNombre)
        val descripcion = dialogView.findViewById<EditText>(R.id.etDescripcion)

        var colorSeleccionado = Color.GRAY

        val rojo = dialogView.findViewById<View>(R.id.colorRojo)
        val azul = dialogView.findViewById<View>(R.id.colorAzul)
        val verde = dialogView.findViewById<View>(R.id.colorVerde)
        val amarillo = dialogView.findViewById<View>(R.id.colorAmarillo)
        val morado = dialogView.findViewById<View>(R.id.colorMorado)

        rojo.setOnClickListener { colorSeleccionado = Color.RED }
        azul.setOnClickListener { colorSeleccionado = Color.BLUE }
        verde.setOnClickListener { colorSeleccionado = Color.GREEN }
        amarillo.setOnClickListener { colorSeleccionado = Color.YELLOW }
        morado.setOnClickListener { colorSeleccionado = Color.MAGENTA }

        AlertDialog.Builder(requireContext())
            .setTitle("Nueva Categoría")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                listaCategorias.add(
                    Categoria(
                        nombre.text.toString(),
                        descripcion.text.toString(),
                        colorSeleccionado
                    )
                )
                adapter.notifyDataSetChanged()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}