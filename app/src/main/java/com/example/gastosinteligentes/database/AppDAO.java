package com.example.gastosinteligentes.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gastosinteligentes.database.entidades.Categoria;
import com.example.gastosinteligentes.database.entidades.Gasto;
import com.example.gastosinteligentes.database.entidades.Presupuesto;
import com.example.gastosinteligentes.database.entidades.Usuario;

import java.util.List;

@Dao
public interface AppDao {

    // =========================
    // USUARIOS
    // =========================

    @Insert
    void insertarUsuario(Usuario usuario);

    @Update
    void actualizarUsuario(Usuario usuario);

    @Delete
    void eliminarUsuario(Usuario usuario);

    @Query("SELECT * FROM usuarios")
    List<Usuario> obtenerUsuarios();

    @Query("SELECT * FROM usuarios WHERE id = :id")
    Usuario obtenerUsuarioPorId(int id);

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    Usuario obtenerUsuarioPorCorreo(String correo);

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND contraseña = :password LIMIT 1")
    Usuario login(String correo, String password);



    // =========================
    // CATEGORIAS
    // =========================

    @Insert
    void insertarCategoria(Categoria categoria);

    @Update
    void actualizarCategoria(Categoria categoria);

    @Delete
    void eliminarCategoria(Categoria categoria);

    @Query("SELECT * FROM categorias")
    List<Categoria> obtenerCategorias();

    @Query("SELECT * FROM categorias WHERE id = :id")
    Categoria obtenerCategoriaPorId(int id);



    // =========================
    // GASTOS
    // =========================

    @Insert
    void insertarGasto(Gasto gasto);

    @Update
    void actualizarGasto(Gasto gasto);

    @Delete
    void eliminarGasto(Gasto gasto);

    @Query("SELECT * FROM gastos")
    List<Gasto> obtenerGastos();

    @Query("SELECT * FROM gastos WHERE id_usuario = :idUsuario")
    List<Gasto> obtenerGastosPorUsuario(int idUsuario);

    @Query("SELECT * FROM gastos WHERE id_categoria = :idCategoria")
    List<Gasto> obtenerGastosPorCategoria(int idCategoria);

    @Query("""
        SELECT * FROM gastos
        WHERE fecha BETWEEN :fechaInicio AND :fechaFin
    """)
    List<Gasto> obtenerGastosPorFechas(
            String fechaInicio,
            String fechaFin
    );

    @Query("""
        SELECT SUM(monto)
        FROM gastos
        WHERE id_usuario = :idUsuario
    """)
    double obtenerTotalGastosUsuario(int idUsuario);

    @Query("""
        SELECT SUM(monto)
        FROM gastos
        WHERE id_usuario = :idUsuario
        AND strftime('%m', fecha) = :mes
        AND strftime('%Y', fecha) = :anio
    """)
    double obtenerTotalGastosMes(
            int idUsuario,
            String mes,
            String anio
    );



    // =========================
    // PRESUPUESTOS
    // =========================

    @Insert
    void insertarPresupuesto(Presupuesto presupuesto);

    @Update
    void actualizarPresupuesto(Presupuesto presupuesto);

    @Delete
    void eliminarPresupuesto(Presupuesto presupuesto);

    @Query("SELECT * FROM presupuestos")
    List<Presupuesto> obtenerPresupuestos();

    @Query("""
        SELECT * FROM presupuestos
        WHERE id_usuario = :idUsuario
        AND mes = :mes
        AND anio = :anio
        LIMIT 1
    """)
    Presupuesto obtenerPresupuestoMensual(
            int idUsuario,
            int mes,
            int anio
    );
}