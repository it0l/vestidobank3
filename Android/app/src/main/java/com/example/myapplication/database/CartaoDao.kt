package com.example.myapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.model.Cartao

@Dao
interface CartaoDao {

    @Query("SELECT * FROM cartoes ORDER BY id DESC")
    suspend fun buscarTodos(): List<Cartao>

    @Query("SELECT * FROM cartoes WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): Cartao?

    @Insert
    suspend fun inserir(cartao: Cartao)

    @Update
    suspend fun atualizar(cartao: Cartao)

    @Delete
    suspend fun deletar(cartao: Cartao)
}
