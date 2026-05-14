package com.example.gastosinteligentes.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gastosinteligentes.database.entidades.Categoria
import com.example.gastosinteligentes.database.entidades.Gasto
import com.example.gastosinteligentes.database.entidades.Presupuesto
import com.example.gastosinteligentes.database.entidades.Usuario

@Dao
interface AppDAO {

    // =========================
    // USUARIOS
    // =========================

    @Insert
    fun insertarUsuario(usuario: Usuario)

    @Update
    fun actualizarUsuario(usuario: Usuario)

    @Delete
    fun eliminarUsuario(usuario: Usuario)

    @Query("SELECT * FROM usuarios")
    fun obtenerUsuarios(): List<Usuario>

    @Query("""
        SELECT * FROM usuarios
        WHERE id = :id
    """)
    fun obtenerUsuarioPorId(
        id: Int
    ): Usuario?

    @Query("""
        SELECT * FROM usuarios
        WHERE correo = :correo
        LIMIT 1
    """)
    fun obtenerUsuarioPorCorreo(
        correo: String
    ): Usuario?

    @Query("""
        SELECT * FROM usuarios
        WHERE correo = :correo
        AND contraseña = :password
        LIMIT 1
    """)
    fun login(
        correo: String,
        password: String
    ): Usuario?



    // =========================
    // CATEGORIAS
    // =========================

    @Insert
    fun insertarCategoria(categoria: Categoria)

    @Update
    fun actualizarCategoria(categoria: Categoria)

    @Delete
    fun eliminarCategoria(categoria: Categoria)

    @Query("""
        SELECT * FROM categorias
        WHERE id_usuario = :idUsuario
    """)
    fun obtenerCategoriasUsuario(
        idUsuario: Int
    ): List<Categoria>

    @Query("""
        SELECT * FROM categorias
        WHERE id = :id
        AND id_usuario = :idUsuario
        LIMIT 1
    """)
    fun obtenerCategoriaPorId(
        id: Int,
        idUsuario: Int
    ): Categoria?

    @Query("""
        SELECT * FROM categorias
        WHERE nombre = :nombre
        AND id_usuario = :idUsuario
        LIMIT 1
    """)
    fun obtenerCategoriaPorNombre(
        nombre: String,
        idUsuario: Int
    ): Categoria?



    // =========================
    // GASTOS
    // =========================

    @Insert
    fun insertarGasto(gasto: Gasto)

    @Update
    fun actualizarGasto(gasto: Gasto)

    @Delete
    fun eliminarGasto(gasto: Gasto)

    @Query("""
        SELECT * FROM gastos
        WHERE id_usuario = :idUsuario
    """)
    fun obtenerGastosPorUsuario(
        idUsuario: Int
    ): List<Gasto>

    @Query("""
        SELECT * FROM gastos
        WHERE id_categoria = :idCategoria
        AND id_usuario = :idUsuario
    """)
    fun obtenerGastosPorCategoria(
        idCategoria: Int,
        idUsuario: Int
    ): List<Gasto>

    @Query("""
        SELECT * FROM gastos
        WHERE fecha BETWEEN :fechaInicio
        AND :fechaFin
        AND id_usuario = :idUsuario
    """)
    fun obtenerGastosPorFechas(
        fechaInicio: String,
        fechaFin: String,
        idUsuario: Int
    ): List<Gasto>

    @Query("""
        SELECT SUM(monto)
        FROM gastos
        WHERE id_usuario = :idUsuario
    """)
    fun obtenerTotalGastosUsuario(
        idUsuario: Int
    ): Double?

    @Query("""
        SELECT SUM(monto)
        FROM gastos
        WHERE id_usuario = :idUsuario
        AND strftime('%m', fecha) = :mes
        AND strftime('%Y', fecha) = :anio
    """)
    fun obtenerTotalGastosMes(
        idUsuario: Int,
        mes: String,
        anio: String
    ): Double?



    // =========================
    // PRESUPUESTOS
    // =========================

    @Insert
    fun insertarPresupuesto(
        presupuesto: Presupuesto
    )

    @Update
    fun actualizarPresupuesto(
        presupuesto: Presupuesto
    )

    @Delete
    fun eliminarPresupuesto(
        presupuesto: Presupuesto
    )

    @Query("""
        SELECT * FROM presupuestos
        WHERE id_usuario = :idUsuario
    """)
    fun obtenerPresupuestosUsuario(
        idUsuario: Int
    ): List<Presupuesto>

    @Query("""
        SELECT * FROM presupuestos
        WHERE id_usuario = :idUsuario
        AND mes = :mes
        AND anio = :anio
        LIMIT 1
    """)
    fun obtenerPresupuestoMensual(
        idUsuario: Int,
        mes: Int,
        anio: Int
    ): Presupuesto?
}