package com.example.myapplication.ui.transacoes

import com.example.myapplication.data.local.Transacao

data class TransacoesUiState(
    val transacoes: List<Transacao> = emptyList(),
    val descricao: String = "",
    val valor: String = "",
    val categoria: String = "",
    val data: String = "",
    val erro: String = "",
    val transacaoDetalhe: Transacao? = null,
    val detalheDescricao: String = "",
    val detalheValor: String = "",
    val detalheCategoria: String = "",
    val detalheData: String = "",
    val mensagemDetalhe: String = ""
)
