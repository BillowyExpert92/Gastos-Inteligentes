package com.example.gastosinteligentes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GastoAdaptador(private val lista:MutableList<Gasto>) :
    RecyclerView.Adapter<GastoAdaptador.ViewHolder>() {

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){

        val descripcion: TextView = view.findViewById(R.id.txtDescripcion)
        val monto:TextView = view.findViewById(R.id.txtMonto)
        val categoria:TextView = view.findViewById(R.id.txtCategoria)
        val fecha:TextView = view.findViewById(R.id.txtFecha)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gasto,parent,false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val gasto = lista[position]

        holder.descripcion.text = gasto.descripcion
        holder.monto.text = "$${gasto.monto}"
        holder.categoria.text = gasto.categoria
        holder.fecha.text = gasto.fecha
    }
}