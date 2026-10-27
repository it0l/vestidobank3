package com.example.myapplication.ui.cartoes

import com.example.myapplication.data.local.Cartao

data class CartoesUiState(
    val cartoes: List<Cartao> = emptyList(),
    val mostrarFormulario: Boolean = false,
    val modoBloqueio: Boolean = false,
    val mensagemAcao: String = "",
    val nome: String = "",
    val finalNumero: String = "",
    val tipo: String = "",
    val limite: String = "",
    val usado: String = "",
    val erro: String = "",
    val cartaoDetalhe: Cartao? = null,
    val novoLimite: String = "",
    val mensagemDetalhe: String = ""
)
