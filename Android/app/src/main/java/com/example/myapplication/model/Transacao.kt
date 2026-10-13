package com.example.myapplication.model

data class Transacao(
    val id: Int,
    val descricao: String,
    val valor: Double,
    val categoria: String,
    val data: String
)
