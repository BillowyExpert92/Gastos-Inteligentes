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
import com.example.gastosinteligentes.database.AppDatabase
import com.example.gastosinteligentes.database.entidades.Categoria
import com.example.gastosinteligentes.utils.SessionManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CategoriasFragment :
    Fragment(R.layout.fragment_categorias) {

    private lateinit var recycler: RecyclerView

    private lateinit var adapter: CategoriaAdapter

    private lateinit var db: AppDatabase

    private val listaCategorias =
        mutableListOf<Categoria>()

    private var idUsuario = -1

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(
            view,
            savedInstanceState
        )

        db = AppDatabase.getDatabase(
            requireContext()
        )

        val session =
            SessionManager(requireContext())

        idUsuario =
            session.obtenerUsuarioId()

        recycler =
            view.findViewById(
                R.id.recyclerCategorias
            )

        val btnAgregar =
            view.findViewById<FloatingActionButton>(
                R.id.btnAgregarCategoria
            )

        adapter = CategoriaAdapter(

            listaCategorias,

            onEliminar = { categoria ->

                db.appDao()
                    .eliminarCategoria(categoria)

                cargarCategorias()
            },

            onEditar = { categoria ->

                mostrarDialogoEditar(categoria)
            }
        )

        recycler.layoutManager =
            LinearLayoutManager(context)

        recycler.adapter = adapter

        cargarCategorias()

        btnAgregar.setOnClickListener {

            mostrarDialogoAgregar()
        }
    }

    // =========================================
    // CARGAR ROOM
    // =========================================

    private fun cargarCategorias() {

        listaCategorias.clear()

        listaCategorias.addAll(
            db.appDao()
                .obtenerCategoriasUsuario(
                    idUsuario
                )
        )

        adapter.notifyDataSetChanged()
    }

    // =========================================
    // DIALOGO AGREGAR
    // =========================================

    private fun mostrarDialogoAgregar() {

        val dialogView =
            LayoutInflater.from(context)
                .inflate(R.layout.dialog_categoria, null)

        val nombre =
            dialogView.findViewById<EditText>(R.id.etNombre)

        val descripcion =
            dialogView.findViewById<EditText>(R.id.etDescripcion)

        var colorSeleccionado =
            android.graphics.Color.GRAY

        configurarColores(
            dialogView,
            colorSeleccionado
        ) { color ->
            colorSeleccionado = color
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Nueva Categoría")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->

                val categoria =
                    Categoria(
                        nombre = nombre.text.toString(),
                        descripcion = descripcion.text.toString(),
                        color = colorSeleccionado,
                        id_usuario = idUsuario
                    )

                db.appDao().insertarCategoria(categoria)

                cargarCategorias()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoEditar(categoria: Categoria) {

        val dialogView =
            LayoutInflater.from(context)
                .inflate(R.layout.dialog_categoria, null)

        val nombre =
            dialogView.findViewById<EditText>(R.id.etNombre)

        val descripcion =
            dialogView.findViewById<EditText>(R.id.etDescripcion)

        nombre.setText(categoria.nombre)
        descripcion.setText(categoria.descripcion)

        var colorSeleccionado =
            categoria.color

        configurarColores(
            dialogView,
            colorSeleccionado
        ) { color ->
            colorSeleccionado = color
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Editar Categoría")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->

                categoria.nombre =
                    nombre.text.toString()

                categoria.descripcion =
                    descripcion.text.toString()

                categoria.color =
                    colorSeleccionado

                db.appDao().actualizarCategoria(categoria)

                cargarCategorias()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun configurarColores(
        dialogView: View,
        colorInicial: Int,
        onColorSeleccionado: (Int) -> Unit
    ) {
        var colorSeleccionado = colorInicial
        onColorSeleccionado(colorSeleccionado)

        val colores = listOf(
            R.id.colorRojo to android.graphics.Color.RED,
            R.id.colorAzul to android.graphics.Color.BLUE,
            R.id.colorVerde to android.graphics.Color.GREEN,
            R.id.colorAmarillo to android.graphics.Color.YELLOW,
            R.id.colorMorado to android.graphics.Color.MAGENTA,

            R.id.colorNaranja to android.graphics.Color.rgb(255, 152, 0),
            R.id.colorRosa to android.graphics.Color.rgb(233, 30, 99),
            R.id.colorCafe to android.graphics.Color.rgb(121, 85, 72),
            R.id.colorGris to android.graphics.Color.rgb(96, 96, 96),
            R.id.colorNegro to android.graphics.Color.BLACK,
            R.id.colorTurquesa to android.graphics.Color.rgb(0, 188, 212),
            R.id.colorLima to android.graphics.Color.rgb(205, 220, 57),
            R.id.colorIndigo to android.graphics.Color.rgb(63, 81, 181),
            R.id.colorVerdeOscuro to android.graphics.Color.rgb(46, 125, 50),
            R.id.colorAzulClaro to android.graphics.Color.rgb(3, 169, 244)
        )

        colores.forEach { (id, color) ->
            val vistaColor = dialogView.findViewById<View>(id)

            vistaColor?.setOnClickListener {
                colorSeleccionado = color
                onColorSeleccionado(colorSeleccionado)
            }
        }
    }
}