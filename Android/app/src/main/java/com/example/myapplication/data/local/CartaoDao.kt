package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CartaoDao {

    @Insert
    suspend fun inserir(cartao: Cartao)

    @Query("SELECT * FROM cartoes ORDER BY id DESC")
    fun observarTodos(): Flow<List<Cartao>>

    @Query("SELECT * FROM cartoes ORDER BY id DESC")
    suspend fun buscarTodos(): List<Cartao>

    @Query("SELECT * FROM cartoes WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): Cartao?

    @Update
    suspend fun atualizar(cartao: Cartao)

    @Delete
    suspend fun deletar(cartao: Cartao)
}
