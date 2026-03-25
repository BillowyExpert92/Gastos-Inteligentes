package com.example.gastosinteligentes.database.entidades;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuarios")
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String correo;
    public String contraseña;
    public String nombre;
}
