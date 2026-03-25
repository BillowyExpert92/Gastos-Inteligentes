package com.example.gastosinteligentes.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.gastosinteligentes.database.entidades.Usuario;
import com.example.gastosinteligentes.database.entidades.Gasto;
import com.example.gastosinteligentes.database.entidades.Categoria;
import com.example.gastosinteligentes.database.entidades.Presupuesto;

@Database(
        entities = {
                Usuario.class,
                Gasto.class,
                Categoria.class,
                Presupuesto.class
        },
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {

        public abstract AppDao appDao();

        private static AppDatabase INSTANCE;

        public static AppDatabase getDatabase(Context context){

                if(INSTANCE == null){

                        INSTANCE = Room.databaseBuilder(
                                        context.getApplicationContext(),
                                        AppDatabase.class,
                                        "gastos_db"
                                ).allowMainThreadQueries()
                                .build();
                }

                return INSTANCE;
        }
}