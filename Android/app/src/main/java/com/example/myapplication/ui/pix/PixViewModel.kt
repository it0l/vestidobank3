package com.example.myapplication.ui.pix

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.ContaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PixViewModel(
    private val contaRepository: ContaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PixUiState())
    val uiState: StateFlow<PixUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            contaRepository.saldoFlow.collect { saldo ->
                _uiState.update {
                    it.copy(saldoAtual = saldo)
                }
            }
        }
    }

    fun onChaveChange(chave: String) {
        _uiState.update {
            it.copy(chave = chave, mensagem = "")
        }
    }

    fun onValorChange(valor: String) {
        _uiState.update {
            it.copy(valor = valor, mensagem = "")
        }
    }

    fun onEnviarPix() {
        val state = _uiState.value
        val valorNumerico = state.valor.replace(",", ".").toDoubleOrNull()

        if (state.chave.isBlank() || valorNumerico == null || valorNumerico <= 0) {
            _uiState.update {
                it.copy(
                    mensagem = "Informe uma chave e um valor válido.",
                    sucesso = false
                )
            }
            return
        }

        val comSucesso = contaRepository.realizarPix(state.chave, valorNumerico)

        if (comSucesso) {
            _uiState.update {
                it.copy(
                    chave = "",
                    valor = "",
                    mensagem = "Pix de R$ %.2f enviado para %s com sucesso!".format(valorNumerico, state.chave),
                    sucesso = true
                )
            }
        } else {
            val saldoFormatado = "R$ %.2f".format(state.saldoAtual).replace('.', ',')
            _uiState.update {
                it.copy(
                    mensagem = "Saldo insuficiente. Seu saldo atual é $saldoFormatado.",
                    sucesso = false
                )
            }
        }
    }
}
