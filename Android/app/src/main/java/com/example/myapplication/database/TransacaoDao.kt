package com.example.myapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.model.Transacao

@Dao
interface TransacaoDao {

    @Query("SELECT * FROM transacoes ORDER BY id DESC")
    suspend fun buscarTodos(): List<Transacao>

    @Query("SELECT * FROM transacoes WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): Transacao?

    @Insert
    suspend fun inserir(transacao: Transacao)

    @Update
    suspend fun atualizar(transacao: Transacao)

    @Delete
    suspend fun deletar(transacao: Transacao)
}
