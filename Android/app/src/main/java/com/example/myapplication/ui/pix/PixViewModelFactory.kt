package com.example.myapplication.ui.pix

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.ContaRepository

class PixViewModelFactory(
    private val repository: ContaRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PixViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PixViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido")
    }
}
