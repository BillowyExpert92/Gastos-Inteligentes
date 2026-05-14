package com.example.gastosinteligentes.database.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "categorias",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Categoria(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var nombre: String = "",

    var descripcion: String = "",

    var id_usuario: Int = 0
)