package com.example.myapplication.ui.fatura

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.TransacaoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FaturaViewModel(
    private val repository: TransacaoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FaturaUiState())
    val uiState: StateFlow<FaturaUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllTransacoes().collect { transacoes ->
                _uiState.update {
                    it.copy(transacoes = transacoes)
                }
            }
        }
    }
}
