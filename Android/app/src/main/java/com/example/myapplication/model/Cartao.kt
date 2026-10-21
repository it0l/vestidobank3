package com.example.myapplication.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "cartoes")
data class Cartao(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val finalNumero: String,
    val tipo: String,
    val limite: Double,
    val usado: Double,
    val bloqueado: Boolean = false
)