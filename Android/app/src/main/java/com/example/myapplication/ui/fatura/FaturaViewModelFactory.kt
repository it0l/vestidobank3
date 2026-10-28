package com.example.myapplication.ui.fatura

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.ContaRepository
import com.example.myapplication.data.repository.TransacaoRepository

class FaturaViewModelFactory(
    private val repository: TransacaoRepository,
    private val contaRepository: ContaRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FaturaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FaturaViewModel(repository, contaRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
