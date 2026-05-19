package com.example.gastosinteligentes

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.gastosinteligentes.database.AppDatabase
import com.example.gastosinteligentes.utils.SessionManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.Locale

class EstadisticasFragment : Fragment(R.layout.fragment_estadisticas) {

    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart
    private lateinit var db: AppDatabase

    private var idUsuario = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pieChart = view.findViewById(R.id.pieChart)
        barChart = view.findViewById(R.id.barChart)

        db = AppDatabase.getDatabase(requireContext())

        val session = SessionManager(requireContext())
        idUsuario = session.obtenerUsuarioId()

        cargarEstadisticas()
    }

    private fun cargarEstadisticas() {
        val gastos = db.appDao().obtenerGastosPorUsuario(idUsuario)

        val gastosPorCategoria = mutableMapOf<String, Float>()
        val coloresCategoria = mutableListOf<Int>()

        val gastosPorMes = mutableMapOf<String, Float>()

        val formatoEntrada = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formatoMes = SimpleDateFormat("MMM yyyy", Locale("es", "MX"))

        for (gasto in gastos) {
            val categoria = db.appDao().obtenerCategoriaPorId(
                gasto.id_categoria,
                idUsuario
            )

            val nombreCategoria = categoria?.nombre ?: "Sin categoría"

            gastosPorCategoria[nombreCategoria] =
                (gastosPorCategoria[nombreCategoria] ?: 0f) + gasto.monto.toFloat()

            if (categoria != null && !coloresCategoria.contains(categoria.color)) {
                coloresCategoria.add(categoria.color)
            }

            val fecha = try {
                formatoEntrada.parse(gasto.fecha)
            } catch (e: Exception) {
                null
            }

            val mes = if (fecha != null) {
                formatoMes.format(fecha)
            } else {
                "Sin fecha"
            }

            gastosPorMes[mes] =
                (gastosPorMes[mes] ?: 0f) + gasto.monto.toFloat()
        }

        cargarPieChart(gastosPorCategoria, coloresCategoria)
        cargarBarChart(gastosPorMes)
    }

    private fun cargarPieChart(
        datos: Map<String, Float>,
        colores: List<Int>
    ) {
        val entries = datos.map {
            PieEntry(it.value, it.key)
        }

        val dataSet = PieDataSet(entries, "Gastos por categoría")

        dataSet.colors =
            if (colores.isNotEmpty()) colores
            else listOf(Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW)

        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.BLACK

        pieChart.data = PieData(dataSet)
        pieChart.description.isEnabled = false
        pieChart.centerText = "Categorías"
        pieChart.setUsePercentValues(false)
        pieChart.invalidate()
    }

    private fun cargarBarChart(datos: Map<String, Float>) {
        val labels = datos.keys.toList()

        val entries = datos.values.mapIndexed { index, monto ->
            BarEntry(index.toFloat(), monto)
        }

        val dataSet = BarDataSet(entries, "Gastos por mes")
        dataSet.valueTextSize = 12f

        barChart.data = BarData(dataSet)
        barChart.description.isEnabled = false

        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.granularity = 1f
        barChart.xAxis.setDrawGridLines(false)

        barChart.axisRight.isEnabled = false
        barChart.invalidate()
    }
}