package com.example.myapplication.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "transacoes")
data class Transacao(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val descricao: String,
    val valor: Double,
    val categoria: String,
    val data: String
)