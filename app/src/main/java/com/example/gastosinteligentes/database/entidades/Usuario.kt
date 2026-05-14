package com.example.gastosinteligentes.database.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var correo: String = "",

    var contraseña: String = "",

    var nombre: String = ""
)