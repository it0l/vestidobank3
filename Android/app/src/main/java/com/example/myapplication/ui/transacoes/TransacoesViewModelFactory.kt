package com.example.myapplication.ui.transacoes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.TransacaoRepository

class TransacoesViewModelFactory(
    private val repository: TransacaoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransacoesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransacoesViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
