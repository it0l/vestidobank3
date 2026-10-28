package com.example.myapplication.data.repository

import android.content.Context
import com.example.myapplication.data.local.Transacao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ContaRepository(
    context: Context,
    private val transacaoRepository: TransacaoRepository
) {
    private val prefs = context.getSharedPreferences("vestidobank_prefs", Context.MODE_PRIVATE)
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _saldoFlow = MutableStateFlow(carregarSaldo())
    val saldoFlow: StateFlow<Double> = _saldoFlow.asStateFlow()

    private fun carregarSaldo(): Double {
        val saldoSalvo = prefs.getFloat(KEY_SALDO, -1f)
        return if (saldoSalvo < 0) {
            SALDO_INICIAL
        } else {
            saldoSalvo.toDouble()
        }
    }

    private fun salvarSaldo(novoSaldo: Double) {
        prefs.edit().putFloat(KEY_SALDO, novoSaldo.toFloat()).apply()
        _saldoFlow.value = novoSaldo
    }

    fun adicionarSaldo(valor: Double) {
        if (valor <= 0) return
        val saldoAtual = _saldoFlow.value
        val novoSaldo = saldoAtual + valor
        salvarSaldo(novoSaldo)

        scope.launch {
            transacaoRepository.inserir(
                Transacao(
                    descricao = "Depósito em conta",
                    valor = valor,
                    categoria = "Depósito",
                    data = dataAtualFormatada()
                )
            )
        }
    }

    fun realizarPix(chave: String, valor: Double): Boolean {
        val saldoAtual = _saldoFlow.value
        if (valor <= 0 || valor > saldoAtual) {
            return false
        }

        val novoSaldo = saldoAtual - valor
        salvarSaldo(novoSaldo)

        scope.launch {
            transacaoRepository.inserir(
                Transacao(
                    descricao = "Pix para $chave",
                    valor = valor,
                    categoria = "Pix",
                    data = dataAtualFormatada()
                )
            )
        }

        return true
    }

    private fun dataAtualFormatada(): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(Date())
    }

    companion object {
        private const val KEY_SALDO = "saldo_conta"
        private const val SALDO_INICIAL = 67.67

        @Volatile
        private var INSTANCE: ContaRepository? = null

        fun getInstance(
            context: Context,
            transacaoRepository: TransacaoRepository
        ): ContaRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = ContaRepository(
                    context.applicationContext,
                    transacaoRepository
                )
                INSTANCE = instance
                instance
            }
        }
    }
}
