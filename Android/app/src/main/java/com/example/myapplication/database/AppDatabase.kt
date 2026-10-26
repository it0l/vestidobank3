package com.example.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.model.Cartao
import com.example.myapplication.model.Transacao

@Database(
    entities = [Transacao::class, Cartao::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transacaoDao(): TransacaoDao
    abstract fun cartaoDao(): CartaoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vestidobank.db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}
