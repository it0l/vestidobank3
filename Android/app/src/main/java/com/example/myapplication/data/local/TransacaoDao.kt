package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TransacaoDao {

    @Insert
    suspend fun inserir(transacao: Transacao)

    @Query("SELECT * FROM transacoes ORDER BY id DESC")
    fun observarTodos(): Flow<List<Transacao>>

    @Query("SELECT * FROM transacoes ORDER BY id DESC")
    suspend fun buscarTodos(): List<Transacao>

    @Query("SELECT * FROM transacoes WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): Transacao?

    @Update
    suspend fun atualizar(transacao: Transacao)

    @Delete
    suspend fun deletar(transacao: Transacao)
}
