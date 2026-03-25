package com.example.gastosinteligentes.database.entidades;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categorias")
public class Categoria {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre;
    public String descripcion;
}
