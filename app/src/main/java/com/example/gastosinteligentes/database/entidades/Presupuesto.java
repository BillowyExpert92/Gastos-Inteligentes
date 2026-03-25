package com.example.gastosinteligentes.database.entidades;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "presupuestos",
        foreignKeys = @ForeignKey(
                entity = Usuario.class,
                parentColumns = "id",
                childColumns = "id_usuario",
                onDelete = ForeignKey.CASCADE
        )
)
public class Presupuesto {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public double monto;
    public int mes;
    public int anio;

    public int id_usuario;
}
