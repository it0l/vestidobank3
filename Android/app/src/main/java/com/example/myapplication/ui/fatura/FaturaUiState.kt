package com.example.myapplication.ui.fatura

import com.example.myapplication.data.local.Transacao

data class FaturaUiState(
    val transacoes: List<Transacao> = emptyList(),
    val limiteTotal: Double = 1000.0,
    val saldoConta: Double = 67.67,
    val totalPagoFatura: Double = 0.0
) {
    val totalCompras: Double
        get() = transacoes.sumOf { it.valor }

    val totalPagoFaturaEfetivo: Double
        get() = totalPagoFatura.coerceIn(0.0, totalCompras)

    val totalFatura: Double
        get() = (totalCompras - totalPagoFaturaEfetivo).coerceAtLeast(0.0)

    val limiteDisponivel: Double
        get() = (limiteTotal - totalFatura).coerceAtLeast(0.0)

    val percentualUtilizado: Float
        get() = if (limiteTotal > 0) {
            (totalFatura / limiteTotal)
                .coerceIn(0.0, 1.0)
                .toFloat()
        } else {
            0f
        }
}
