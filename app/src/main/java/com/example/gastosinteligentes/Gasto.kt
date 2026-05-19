package com.example.gastosinteligentes

data class Gasto(
    val id: Int,
    val descripcion: String,
    val monto: Double,
    val categoria: String,
    val fecha: String,
    val colorCategoria: Int
)