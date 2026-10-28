package com.example.myapplication.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.ContaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val contaRepository: ContaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            contaRepository.saldoFlow.collect { novoSaldo ->
                _uiState.update {
                    it.copy(saldo = novoSaldo)
                }
            }
        }
    }

    fun onAbrirPopUpTrazerDinheiro() {
        _uiState.update {
            it.copy(mostrarPopUpTrazerDinheiro = true)
        }
    }

    fun onFecharPopUpTrazerDinheiro() {
        _uiState.update {
            it.copy(mostrarPopUpTrazerDinheiro = false)
        }
    }

    fun onConfirmarTrazerDinheiro(valor: Double) {
        contaRepository.adicionarSaldo(valor)
        onFecharPopUpTrazerDinheiro()
    }
}
