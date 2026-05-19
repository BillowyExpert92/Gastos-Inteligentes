package com.example.gastosinteligentes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gastosinteligentes.database.entidades.Categoria
import com.example.gastosinteligentes.database.entidades.Gasto
import com.example.gastosinteligentes.database.entidades.Presupuesto
import com.example.gastosinteligentes.database.entidades.Usuario

@Database(
    entities = [
        Usuario::class,
        Categoria::class,
        Gasto::class,
        Presupuesto::class
    ],

    // SUBIR VERSION
    version = 3
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDAO

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context
        ): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "gastos_db"
                    )

                        // IMPORTANTE
                        .fallbackToDestructiveMigration()

                        .allowMainThreadQueries()

                        .build()

                INSTANCE = instance

                instance
            }
        }
    }
}