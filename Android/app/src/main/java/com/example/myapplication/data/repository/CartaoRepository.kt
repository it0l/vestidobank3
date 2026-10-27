package com.example.myapplication.data.repository

import com.example.myapplication.data.local.Cartao
import com.example.myapplication.data.local.CartaoDao
import kotlinx.coroutines.flow.Flow

class CartaoRepository(
    private val cartaoDao: CartaoDao
) {
    fun getAllCartoes(): Flow<List<Cartao>> {
        return cartaoDao.observarTodos()
    }

    suspend fun buscarTodos(): List<Cartao> {
        return cartaoDao.buscarTodos()
    }

    suspend fun buscarPorId(id: Int): Cartao? {
        return cartaoDao.buscarPorId(id)
    }

    suspend fun inserir(cartao: Cartao) {
        cartaoDao.inserir(cartao)
    }

    suspend fun atualizar(cartao: Cartao) {
        cartaoDao.atualizar(cartao)
    }

    suspend fun deletar(cartao: Cartao) {
        cartaoDao.deletar(cartao)
    }
}
