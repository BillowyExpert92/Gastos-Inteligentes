package com.example.gastosinteligentes.database.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "presupuestos",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Presupuesto(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var monto: Double = 0.0,

    var mes: Int = 0,

    var anio: Int = 0,

    var id_usuario: Int = 0
)