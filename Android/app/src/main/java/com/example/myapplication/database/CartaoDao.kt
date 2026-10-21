package com.example.myapplication.database

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.myapplication.model.Cartao
import kotlinx.coroutines.flow.Flow

@Dao
interface CartaoDao {

    @Query("SELECT * FROM cartoes ORDER BY id DESC")
    fun listarTodos(): Flow<List<Cartao>>

    @Query("SELECT * FROM cartoes WHERE id = :id LIMIT 1")
    fun buscarPorId(id: Int): Flow<Cartao?>

    @Insert
    suspend fun inserir(cartao: Cartao)

    @Update
    suspend fun atualizar(cartao: Cartao)

    @Delete
    suspend fun remover(cartao: Cartao)
}