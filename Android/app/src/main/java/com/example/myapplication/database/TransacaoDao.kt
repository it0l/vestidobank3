package com.example.myapplication.database

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import com.example.myapplication.model.Transacao
import kotlinx.coroutines.flow.Flow

@Dao
interface TransacaoDao {

    @Query("SELECT * FROM transacoes ORDER BY id DESC")
    fun listarTodas(): Flow<List<Transacao>>

    @Query("SELECT * FROM transacoes WHERE id = :id LIMIT 1")
    fun buscarPorId(id: Int): Flow<Transacao?>

    @Insert
    suspend fun inserir(transacao: Transacao)

    @Delete
    suspend fun remover(transacao: Transacao)
}