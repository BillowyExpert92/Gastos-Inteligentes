package com.example.gastosinteligentes.utils

import android.content.Context

class SessionManager(context: Context) {

    private val prefs =
        context.getSharedPreferences(
            "sesion",
            Context.MODE_PRIVATE
        )

    fun guardarUsuario(idUsuario: Int){

        prefs.edit()
            .putInt(
                "id_usuario",
                idUsuario
            )
            .apply()
    }

    fun obtenerUsuarioId(): Int {

        return prefs.getInt(
            "id_usuario",
            -1
        )
    }
    fun cerrarSesion() {

        prefs.edit()
            .clear()
            .apply()
    }
}