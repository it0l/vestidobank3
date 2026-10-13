package com.example.myapplication.model

data class Cartao(
    val id: Int,
    val nome: String,
    val finalNumero: String,
    val tipo: String,
    val limite: Double,
    val usado: Double,
    val bloqueado: Boolean = false
)
