package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

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
