package com.example.gastosinteligentes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EstadisticasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EstadisticasFragment : Fragment(R.layout.fragment_estadisticas) {

    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_estadisticas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        pieChart = view.findViewById(R.id.pieChart)
        barChart = view.findViewById(R.id.barChart)

        val datos = obtenerDatosGastos()

        cargarPieChart(datos.first)
        cargarBarChart(datos.second)
    }

    private fun obtenerDatosGastos(): Pair<List<Pair<String, Float>>, List<Pair<String, Float>>> {

        // Aquí deberías recibir datos reales desde BD/API

        val hayDatos = false

        return if (hayDatos) {
            Pair(listOf(), listOf())
        } else {
            // DATOS DUMMY
            Pair(
                listOf(
                    "Comida" to 300f,
                    "Transporte" to 150f,
                    "Mercado" to 200f,
                    "Hogar" to 100f,
                    "Escuela" to 80f
                ),
                listOf(
                    "Enero" to 500f,
                    "Febrero" to 700f,
                    "Marzo" to 650f
                )
            )
        }
    }

    private fun cargarPieChart(data: List<Pair<String, Float>>) {

        val entries = data.map {
            PieEntry(it.second, it.first)
        }

        val dataSet = PieDataSet(entries, "Gastos")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

        pieChart.data = PieData(dataSet)
        pieChart.invalidate()
    }

    private fun cargarBarChart(data: List<Pair<String, Float>>) {

        val entries = data.mapIndexed { index, pair ->
            BarEntry(index.toFloat(), pair.second)
        }

        val dataSet = BarDataSet(entries, "Mensual")

        barChart.data = BarData(dataSet)
        barChart.invalidate()
    }
}