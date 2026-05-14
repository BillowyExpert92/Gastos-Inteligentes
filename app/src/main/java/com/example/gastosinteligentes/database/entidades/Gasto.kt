package com.example.gastosinteligentes.database.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "gastos",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Categoria::class,
            parentColumns = ["id"],
            childColumns = ["id_categoria"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Gasto(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var monto: Double = 0.0,

    var descripcion: String = "",

    var hora: String = "",

    var fecha: String = "",

    var id_usuario: Int = 0,

    var id_categoria: Int = 0
)