package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transacoes")
data class Transacao(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val descricao: String,
    val valor: Double,
    val categoria: String,
    val data: String
)
