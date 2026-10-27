package com.example.myapplication.data.repository

import com.example.myapplication.data.local.Transacao
import com.example.myapplication.data.local.TransacaoDao
import kotlinx.coroutines.flow.Flow

class TransacaoRepository(
    private val transacaoDao: TransacaoDao
) {
    fun getAllTransacoes(): Flow<List<Transacao>> {
        return transacaoDao.observarTodos()
    }

    suspend fun buscarTodos(): List<Transacao> {
        return transacaoDao.buscarTodos()
    }

    suspend fun buscarPorId(id: Int): Transacao? {
        return transacaoDao.buscarPorId(id)
    }

    suspend fun inserir(transacao: Transacao) {
        transacaoDao.inserir(transacao)
    }

    suspend fun atualizar(transacao: Transacao) {
        transacaoDao.atualizar(transacao)
    }

    suspend fun deletar(transacao: Transacao) {
        transacaoDao.deletar(transacao)
    }
}
