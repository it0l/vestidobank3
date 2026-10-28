package com.example.myapplication.navigation

object Rotas {
    const val HOME = "home"
    const val FATURA = "fatura"
    const val TRANSACOES = "transacoes"
    const val CARTOES = "cartoes"
    const val PIX = "pix"
    const val TRAZER_DINHEIRO = "trazer_dinheiro"
    const val DETALHE_TRANSACAO = "detalhe_transacao"
    const val DETALHE_CARTAO = "detalhe_cartao"

    fun detalheTransacao(id: Int): String {
        return "$DETALHE_TRANSACAO/$id"
    }

    fun detalheCartao(id: Int): String {
        return "$DETALHE_CARTAO/$id"
    }
}
