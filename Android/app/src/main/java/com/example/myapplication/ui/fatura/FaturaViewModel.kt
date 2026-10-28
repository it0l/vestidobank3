package com.example.myapplication.ui.fatura

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.ContaRepository
import com.example.myapplication.data.repository.TransacaoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FaturaViewModel(
    private val repository: TransacaoRepository,
    private val contaRepository: ContaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FaturaUiState())
    val uiState: StateFlow<FaturaUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllTransacoes().collect { transacoes ->
                val transacoesFatura = transacoes.filter {
                    it.categoria != "Pix" && it.categoria != "Depósito"
                }
                val totalCompras = transacoesFatura.sumOf { it.valor }
                val totalPagoAtual = contaRepository.totalPagoFaturaFlow.value

                if (totalPagoAtual > totalCompras) {
                    contaRepository.atualizarTotalPagoFatura(totalCompras.coerceAtLeast(0.0))
                }

                _uiState.update {
                    it.copy(transacoes = transacoesFatura)
                }
            }
        }

        viewModelScope.launch {
            contaRepository.saldoFlow.collect { saldo ->
                _uiState.update {
                    it.copy(saldoConta = saldo)
                }
            }
        }

        viewModelScope.launch {
            contaRepository.totalPagoFaturaFlow.collect { totalPago ->
                val totalCompras = _uiState.value.totalCompras
                val totalPagoAjustado = totalPago.coerceIn(0.0, totalCompras)

                _uiState.update {
                    it.copy(totalPagoFatura = totalPagoAjustado)
                }
            }
        }
    }

    fun pagarFatura(valorTexto: String): Pair<Boolean, String> {
        val total = _uiState.value.totalFatura

        if (total <= 0) {
            return Pair(false, "A fatura já está totalmente paga!")
        }

        val valorNumerico = if (valorTexto.isBlank()) {
            total
        } else {
            valorTexto.replace(",", ".").toDoubleOrNull()
        }

        if (valorNumerico == null || valorNumerico <= 0) {
            return Pair(false, "Informe um valor válido para pagamento.")
        }

        if (valorNumerico > total) {
            val totalFormatado = "R$ %.2f".format(total).replace('.', ',')
            return Pair(false, "O valor do pagamento não pode ser maior do que a fatura ($totalFormatado).")
        }

        val saldoAtual = _uiState.value.saldoConta
        if (valorNumerico > saldoAtual) {
            val saldoFormatado = "R$ %.2f".format(saldoAtual).replace('.', ',')
            return Pair(false, "Saldo insuficiente em conta ($saldoFormatado).")
        }

        val sucesso = contaRepository.pagarFatura(valorNumerico)
        return if (sucesso) {
            val valorFormatado = "R$ %.2f".format(valorNumerico).replace('.', ',')
            Pair(true, "Pagamento de $valorFormatado realizado com sucesso!")
        } else {
            Pair(false, "Erro ao processar o pagamento.")
        }
    }
}
