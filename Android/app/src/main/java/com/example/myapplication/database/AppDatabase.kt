package com.example.myapplication.database

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.example.myapplication.model.Cartao
import com.example.myapplication.model.Transacao

@Database(
    entities = [
        Transacao::class,
        Cartao::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transacaoDao(): TransacaoDao

    abstract fun cartaoDao(): CartaoDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance =
                    Room.databaseBuilder<AppDatabase>(
                        context.applicationContext,
                        "vestidobank.db"
                    )
                        .setDriver(AndroidSQLiteDriver())
                        .build()

                INSTANCE = instance

                instance
            }
        }
    }
}