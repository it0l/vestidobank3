package com.example.myapplication.ui.fatura

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.TransacaoRepository

class FaturaViewModelFactory(
    private val repository: TransacaoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FaturaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FaturaViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
