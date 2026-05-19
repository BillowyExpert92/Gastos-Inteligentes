package com.example.gastosinteligentes

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gastosinteligentes.database.AppDatabase
import com.example.gastosinteligentes.database.entidades.Presupuesto
import com.example.gastosinteligentes.utils.SessionManager
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import androidx.recyclerview.widget.ItemTouchHelper
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import java.text.ParseException
import java.util.Collections

class InicioFragment : Fragment(R.layout.fragment_inicio) {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: GastoAdaptador
    private lateinit var db: AppDatabase

    private var listaGastos = mutableListOf<Gasto>()

    private lateinit var txtPresupuesto: TextView
    private lateinit var txtGastado: TextView
    private lateinit var txtDisponible: TextView
    private lateinit var progreso: LinearProgressIndicator
    private lateinit var cardBudget: MaterialCardView

    private var presupuestoActual: Presupuesto? = null
    private var idUsuario = -1

    private lateinit var chipGroupCategorias: ChipGroup

    private var listaOriginalGastos =
        mutableListOf<Gasto>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getDatabase(requireContext())

        val session = SessionManager(requireContext())
        idUsuario = session.obtenerUsuarioId()

        recycler = view.findViewById(R.id.listaGastos)
        adapter = GastoAdaptador(listaGastos)

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        configurarSwipeEliminar()

        txtPresupuesto = view.findViewById(R.id.txtPresupuesto)
        txtGastado = view.findViewById(R.id.txtGastado)
        txtDisponible = view.findViewById(R.id.txtDisponible)
        progreso = view.findViewById(R.id.progreso)
        cardBudget = view.findViewById(R.id.cardBudget)

        chipGroupCategorias =
            view.findViewById(R.id.chipGroupCategorias)

        val btnAgregar = view.findViewById<FloatingActionButton>(R.id.btnAgregar)

        btnAgregar.setOnClickListener {
            mostrarDialogo()
        }

        cardBudget.setOnClickListener {
            mostrarDialogoPresupuesto()
        }

        cargarPresupuesto()
        cargarGastos()
        configurarFiltrosCategorias()
        actualizarCard()
    }

    private fun cargarPresupuesto() {
        val calendar = Calendar.getInstance()
        val mes = calendar.get(Calendar.MONTH) + 1
        val anio = calendar.get(Calendar.YEAR)

        presupuestoActual = db.appDao()
            .obtenerPresupuestoMensual(idUsuario, mes, anio)

        actualizarCard()
    }

    private fun mostrarDialogoPresupuesto() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_presupuesto, null)

        val editMonto = dialogView.findViewById<TextInputEditText>(
            R.id.editMontoPresupuesto
        )

        val btnCancelar = dialogView.findViewById<Button>(
            R.id.btnCancelarPresupuesto
        )

        val btnAceptar = dialogView.findViewById<Button>(
            R.id.btnAceptarPresupuesto
        )

        if (presupuestoActual != null) {
            editMonto.setText(presupuestoActual!!.monto.toString())
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialog.show()

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        btnAceptar.setOnClickListener {
            val montoTexto = editMonto.text.toString().trim()

            if (montoTexto.isEmpty()) {
                editMonto.error = "Ingresa un monto"
                return@setOnClickListener
            }

            val monto = montoTexto.toDoubleOrNull()

            if (monto == null) {
                editMonto.error = "Monto inválido"
                return@setOnClickListener
            }

            val calendar = Calendar.getInstance()
            val mes = calendar.get(Calendar.MONTH) + 1
            val anio = calendar.get(Calendar.YEAR)

            if (presupuestoActual == null) {
                val nuevoPresupuesto = Presupuesto(
                    monto = monto,
                    mes = mes,
                    anio = anio,
                    id_usuario = idUsuario
                )

                db.appDao().insertarPresupuesto(nuevoPresupuesto)

                presupuestoActual = db.appDao()
                    .obtenerPresupuestoMensual(idUsuario, mes, anio)
            } else {
                presupuestoActual!!.monto = monto
                db.appDao().actualizarPresupuesto(presupuestoActual!!)
            }

            actualizarCard()
            dialog.dismiss()
        }
    }

    private fun cargarGastos() {

        listaGastos.clear()

        listaOriginalGastos.clear()

        val gastosBD =
            db.appDao()
                .obtenerGastosPorUsuario(idUsuario)

        for (gastoBD in gastosBD) {

            val categoria =
                db.appDao()
                    .obtenerCategoriaPorId(
                        gastoBD.id_categoria,
                        idUsuario
                    )

            val gasto = Gasto(
                id = gastoBD.id,
                descripcion = gastoBD.descripcion,
                monto = gastoBD.monto,
                categoria = categoria?.nombre ?: "Sin categoría",
                fecha = gastoBD.fecha,
                colorCategoria =
                    categoria?.color
                        ?: android.graphics.Color.GRAY
            )

            listaGastos.add(gasto)

            listaOriginalGastos.add(gasto)
        }


        listaGastos.sortByDescending {
            convertirFecha(it.fecha)
        }

        listaOriginalGastos.sortByDescending {
            convertirFecha(it.fecha)
        }

        adapter.notifyDataSetChanged()

        actualizarCard()
    }

    private fun actualizarCard() {

        val presupuesto =
            presupuestoActual?.monto ?: 0.0

        val gastado =
            listaGastos.sumOf { it.monto }

        val disponible =
            presupuesto - gastado

        val porcentaje =
            if (presupuesto > 0) {
                ((gastado / presupuesto) * 100).toInt()
            } else {
                0
            }

        val formato =
            NumberFormat.getCurrencyInstance(
                Locale("es", "MX")
            )

        txtPresupuesto.text =
            formato.format(presupuesto)

        txtGastado.text =
            "Gastado: ${formato.format(gastado)}"

        txtDisponible.text =
            "Restante: ${formato.format(disponible)}"

        progreso.progress =
            porcentaje.coerceAtMost(100)

        when {

            gastado > presupuesto -> {

                // ROJO
                cardBudget.setCardBackgroundColor(
                    android.graphics.Color.parseColor("#FCA5A5")
                )
            }

            porcentaje >= 80 -> {

                // AMARILLO
                cardBudget.setCardBackgroundColor(
                    android.graphics.Color.parseColor("#FDE68A")
                )
            }

            else -> {

                // AZUL NORMAL
                cardBudget.setCardBackgroundColor(
                    android.graphics.Color.parseColor("#2563EB")
                )
            }

        }
    }

    private fun mostrarDialogo() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_agregar_gasto, null)

        val edtMonto = dialogView.findViewById<EditText>(R.id.edtMonto)
        val edtDescripcion = dialogView.findViewById<EditText>(R.id.edtDescripcion)
        val spinner = dialogView.findViewById<Spinner>(R.id.spinnerCategoria)
        val edtFecha = dialogView.findViewById<EditText>(R.id.edtFecha)
        val btnGuardar = dialogView.findViewById<View>(R.id.btnGuardar)

        val categorias = db.appDao()
            .obtenerCategoriasUsuario(idUsuario)

        if (categorias.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Primero agrega una categoría",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val nombresCategorias = categorias.map { it.nombre }

        val adapterCategorias = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            nombresCategorias
        )

        adapterCategorias.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinner.adapter = adapterCategorias

        val hoy = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()
        ).format(Date())

        edtFecha.setText(hoy)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialog.show()

        btnGuardar.setOnClickListener {
            val monto = edtMonto.text.toString().toDoubleOrNull()
            val descripcion = edtDescripcion.text.toString().trim()
            val fecha = edtFecha.text.toString().trim()

            if (monto == null) {
                edtMonto.error = "Ingresa un monto válido"
                return@setOnClickListener
            }

            if (descripcion.isEmpty()) {
                edtDescripcion.error = "Ingresa una descripción"
                return@setOnClickListener
            }

            if (fecha.isEmpty()) {
                edtFecha.error = "Ingresa una fecha"
                return@setOnClickListener
            }

            if (!fechaValida(fecha)) {
                edtFecha.error = "Formato inválido. Usa dd/mm/aaaa"
                return@setOnClickListener
            }

            val categoriaSeleccionada =
                categorias[spinner.selectedItemPosition]

            val gastoEntidad =
                com.example.gastosinteligentes.database.entidades.Gasto(
                    monto = monto,
                    descripcion = descripcion,
                    fecha = fecha,
                    hora = "",
                    id_usuario = idUsuario,
                    id_categoria = categoriaSeleccionada.id
                )

            db.appDao().insertarGasto(gastoEntidad)

            cargarGastos()

            configurarFiltrosCategorias()

            actualizarCard()

            dialog.dismiss()
        }
    }

    private fun configurarFiltrosCategorias() {

        chipGroupCategorias.removeAllViews()

        val chipTodas =
            Chip(requireContext())

        chipTodas.text = "Todas"

        chipTodas.isCheckable = true

        chipTodas.isChecked = true

        chipTodas.setOnClickListener {

            filtrarGastos("Todas")
        }

        chipGroupCategorias.addView(chipTodas)

        val categorias =
            db.appDao()
                .obtenerCategoriasUsuario(idUsuario)

        for (categoria in categorias) {

            val chip =
                Chip(requireContext())

            chip.text = categoria.nombre

            chip.isCheckable = true

            chip.setOnClickListener {

                filtrarGastos(
                    categoria.nombre
                )
            }

            chipGroupCategorias.addView(chip)
        }
    }

    private fun filtrarGastos(
        categoria: String
    ) {

        listaGastos.clear()

        if (categoria == "Todas") {

            listaGastos.addAll(
                listaOriginalGastos
            )

        } else {

            listaGastos.addAll(

                listaOriginalGastos.filter {

                    it.categoria == categoria
                }
            )
        }

        adapter.notifyDataSetChanged()
    }

    private fun configurarSwipeEliminar() {

        val itemTouchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT
            ) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun getSwipeThreshold(
                    viewHolder: RecyclerView.ViewHolder
                ): Float {
                    return 0.60f
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    val position = viewHolder.adapterPosition

                    if (position == RecyclerView.NO_POSITION) {
                        return
                    }

                    val gasto = listaGastos[position]

                    AlertDialog.Builder(requireContext())
                        .setTitle("Eliminar gasto")
                        .setMessage("¿Seguro que quieres eliminar \"${gasto.descripcion}\"?")
                        .setPositiveButton("Eliminar") { _, _ ->

                            val gastoBD = db.appDao()
                                .obtenerGastosPorUsuario(idUsuario)
                                .find { it.id == gasto.id }

                            if (gastoBD != null) {
                                db.appDao().eliminarGasto(gastoBD)
                            }

                            listaGastos.removeAt(position)

                            listaOriginalGastos.removeAll {
                                it.id == gasto.id
                            }

                            adapter.notifyItemRemoved(position)

                            actualizarCard()
                            configurarFiltrosCategorias()
                        }
                        .setNegativeButton("Cancelar") { _, _ ->

                            adapter.notifyItemChanged(position)
                        }
                        .setOnCancelListener {

                            adapter.notifyItemChanged(position)
                        }
                        .show()
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    val itemView = viewHolder.itemView

                    val maxSwipe = itemView.width * 0.60f

                    val newDx = if (dX < -maxSwipe) {
                        -maxSwipe
                    } else {
                        dX
                    }

                    val background = ColorDrawable(Color.parseColor("#EF4444"))

                    background.setBounds(
                        itemView.right + newDx.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )

                    background.draw(c)

                    val paint = Paint()
                    paint.color = Color.WHITE
                    paint.textSize = 38f
                    paint.isFakeBoldText = true
                    paint.textAlign = Paint.Align.RIGHT

                    val icon = ContextCompat.getDrawable(
                        requireContext(),
                        android.R.drawable.ic_menu_delete
                    )

                    val iconSize = 55
                    val iconMargin = 40

                    val iconTop =
                        itemView.top + (itemView.height - iconSize) / 2

                    val iconLeft =
                        itemView.right - iconMargin - iconSize

                    val iconRight =
                        itemView.right - iconMargin

                    val iconBottom =
                        iconTop + iconSize

                    icon?.setBounds(
                        iconLeft,
                        iconTop,
                        iconRight,
                        iconBottom
                    )

                    icon?.setTint(Color.WHITE)
                    icon?.draw(c)

                    c.drawText(
                        "Eliminar",
                        itemView.right - 115f,
                        itemView.top + itemView.height / 2f + 13f,
                        paint
                    )

                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        newDx,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }
        )

        itemTouchHelper.attachToRecyclerView(recycler)
    }

    private fun convertirFecha(fecha: String): Date? {
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        formato.isLenient = false

        return try {
            formato.parse(fecha)
        } catch (e: ParseException) {
            null
        }
    }

    private fun fechaValida(fecha: String): Boolean {
        return convertirFecha(fecha) != null
    }
}