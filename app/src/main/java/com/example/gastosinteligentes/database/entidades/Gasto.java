package com.example.gastosinteligentes.database.entidades;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "gastos",
        foreignKeys = {
                @ForeignKey(
                        entity = Usuario.class,
                        parentColumns = "id",
                        childColumns = "id_usuario",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Categoria.class,
                        parentColumns = "id",
                        childColumns = "id_categoria",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class Gasto {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public double monto;

    public String descripcion;

    public String hora;

    public String fecha;

    public int id_usuario;

    public int id_categoria;
}
