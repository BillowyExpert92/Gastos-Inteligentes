package com.example.gastosinteligentes

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gastosinteligentes.database.entidades.Categoria
import com.google.android.material.card.MaterialCardView

class CategoriaAdapter(

    private val lista: MutableList<Categoria>,

    private val onEliminar: (Categoria) -> Unit,

    private val onEditar: (Categoria) -> Unit

) : RecyclerView.Adapter<CategoriaAdapter.ViewHolder>() {

    inner class ViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        val nombre: TextView =
            view.findViewById(R.id.txtNombre)

        val descripcion: TextView =
            view.findViewById(R.id.txtDescripcion)

        val btnEditar: Button =
            view.findViewById(R.id.btnEditar)

        val btnEliminar: Button =
            view.findViewById(R.id.btnEliminar)

        val card: MaterialCardView =
            view as MaterialCardView

        val lineaColor: View =
            view.findViewById(R.id.lineaColor)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_categoria,
                    parent,
                    false
                )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {

        return lista.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val categoria = lista[position]

        holder.nombre.text =
            categoria.nombre

        holder.descripcion.text =
            categoria.descripcion

        holder.lineaColor.setBackgroundColor(
            categoria.color
        )

        holder.card.setCardBackgroundColor(
            Color.WHITE
        )

        // =====================================
        // ELIMINAR
        // =====================================

        holder.btnEliminar.setOnClickListener {

            onEliminar(categoria)
        }

        // =====================================
        // EDITAR
        // =====================================

        holder.btnEditar.setOnClickListener {

            onEditar(categoria)
        }
    }
}