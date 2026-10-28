package com.example.myapplication.data.repository

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ContaRepository(
    context: Context
) {
    private val prefs = context.getSharedPreferences("vestidobank_prefs", Context.MODE_PRIVATE)

    private val _saldoFlow = MutableStateFlow(carregarSaldo())
    val saldoFlow: StateFlow<Double> = _saldoFlow.asStateFlow()

    private val _totalPagoFaturaFlow = MutableStateFlow(carregarTotalPagoFatura())
    val totalPagoFaturaFlow: StateFlow<Double> = _totalPagoFaturaFlow.asStateFlow()

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

    private fun carregarTotalPagoFatura(): Double {
        return prefs.getFloat(KEY_TOTAL_PAGO_FATURA, 0f).toDouble()
    }

    private fun salvarTotalPagoFatura(novoTotal: Double) {
        prefs.edit().putFloat(KEY_TOTAL_PAGO_FATURA, novoTotal.toFloat()).apply()
        _totalPagoFaturaFlow.value = novoTotal
    }

    fun atualizarTotalPagoFatura(novoTotal: Double) {
        val totalValido = novoTotal.coerceAtLeast(0.0)
        salvarTotalPagoFatura(totalValido)
    }

    fun adicionarSaldo(valor: Double) {
        if (valor <= 0) return
        val saldoAtual = _saldoFlow.value
        val novoSaldo = saldoAtual + valor
        salvarSaldo(novoSaldo)
    }

    fun realizarPix(chave: String, valor: Double): Boolean {
        val saldoAtual = _saldoFlow.value
        if (valor <= 0 || valor > saldoAtual) {
            return false
        }

        val novoSaldo = saldoAtual - valor
        salvarSaldo(novoSaldo)
        return true
    }

    fun pagarFatura(valor: Double): Boolean {
        val saldoAtual = _saldoFlow.value
        if (valor <= 0 || valor > saldoAtual) {
            return false
        }

        val novoSaldo = saldoAtual - valor
        salvarSaldo(novoSaldo)

        val novoTotalPago = _totalPagoFaturaFlow.value + valor
        salvarTotalPagoFatura(novoTotalPago)

        return true
    }

    companion object {
        private const val KEY_SALDO = "saldo_conta"
        private const val KEY_TOTAL_PAGO_FATURA = "total_pago_fatura"
        private const val SALDO_INICIAL = 67.67

        @Volatile
        private var INSTANCE: ContaRepository? = null

        fun getInstance(
            context: Context,
            transacaoRepository: TransacaoRepository? = null
        ): ContaRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = ContaRepository(
                    context.applicationContext
                )
                INSTANCE = instance
                instance
            }
        }
    }
}
