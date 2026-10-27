package com.example.myapplication.ui.cartoes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.CartaoRepository

class CartoesViewModelFactory(
    private val repository: CartaoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartoesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartoesViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
