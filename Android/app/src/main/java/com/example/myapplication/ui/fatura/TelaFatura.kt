package com.example.myapplication.ui.fatura

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.Transacao
import com.example.myapplication.data.repository.ContaRepository
import com.example.myapplication.data.repository.TransacaoRepository

private val Background = Color(0xFFF5F5F7)
private val VestidoPink = Color(0xFFE31A6B)
private val SoftPink = Color(0xFFFFE7F0)
private val CardWhite = Color.White
private val TextColor = Color(0xFF151515)
private val SecondaryText = Color(0xFF6D6D72)
private val DividerColor = Color(0xFFE8E8EC)
private val SuccessGreen = Color(0xFF2F8F5B)

@Composable
fun TelaFatura(
    onBack: () -> Unit,
    onAcessarFatura: () -> Unit,
    onMeusLimites: () -> Unit,
    viewModel: FaturaViewModel = viewModel(
        factory = FaturaViewModelFactory(
            TransacaoRepository(
                AppDatabase.getDatabase(LocalContext.current).transacaoDao()
            ),
            ContaRepository.getInstance(
                LocalContext.current
            )
        )
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var mostrarPagamento by remember {
        mutableStateOf(false)
    }

    var valorPagamento by remember {
        mutableStateOf("")
    }

    var mostrarScore by remember {
        mutableStateOf(false)
    }

    Scaffold(
        containerColor = Background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 28.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 8.dp,
                        top = 6.dp,
                        end = 20.dp,
                        bottom = 8.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = TextColor
                    )
                }

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(SoftPink)
                        .padding(
                            horizontal = 12.dp,
                            vertical = 7.dp
                        )
                ) {
                    Text(
                        "FATURA",
                        color = VestidoPink,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier.padding(
                    horizontal = 20.dp
                )
            ) {
                Text(
                    "Minha fatura",
                    color = TextColor,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                ResumoFatura(
                    total = uiState.totalFatura,
                    limiteTotal = uiState.limiteTotal,
                    disponivel = uiState.limiteDisponivel,
                    percentual = uiState.percentualUtilizado
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                AcoesRapidas(
                    onPagarFatura = {
                        mostrarPagamento = true
                    },
                    onAcessarFatura = onAcessarFatura,
                    onVestidoScore = {
                        mostrarScore = true
                    },
                    onMeusLimites = onMeusLimites
                )

                Spacer(
                    modifier = Modifier.height(22.dp)
                )

                Text(
                    "Movimentações da fatura",
                    color = TextColor,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CardWhite
                    )
                ) {
                    if (uiState.transacoes.isEmpty()) {
                        Text(
                            text = "Nenhuma movimentação cadastrada.",
                            modifier = Modifier.padding(18.dp),
                            color = SecondaryText
                        )
                    } else {
                        uiState.transacoes.forEachIndexed { index, transacao ->
                            CompraFatura(
                                transacao = transacao
                            )

                            if (
                                index < uiState.transacoes.lastIndex
                            ) {
                                HorizontalDivider(
                                    color = DividerColor,
                                    modifier = Modifier.padding(
                                        horizontal = 18.dp
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CardWhite
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Text(
                            "Resumo",
                            color = TextColor,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        LinhaData(
                            "Movimentações cadastradas",
                            uiState.transacoes.size.toString()
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        LinhaData(
                            "Total da fatura",
                            "R$ %.2f".format(
                                uiState.totalFatura
                            )
                        )
                    }
                }
            }
        }
    }

    if (mostrarPagamento) {
        AlertDialog(
            onDismissRequest = {
                mostrarPagamento = false
            },
            title = {
                Text("Pagar fatura")
            },
            text = {
                Column {
                    Text(
                        "Fatura atual: R$ %.2f".format(
                            uiState.totalFatura
                        ),
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        "Saldo em conta: R$ %.2f".format(
                            uiState.saldoConta
                        ),
                        fontSize = 13.sp,
                        color = SecondaryText
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    OutlinedTextField(
                        value = valorPagamento,
                        onValueChange = {
                            valorPagamento = it
                        },
                        label = {
                            Text("Valor do pagamento")
                        },
                        prefix = {
                            Text("R$ ")
                        },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val (sucesso, mensagem) = viewModel.pagarFatura(valorPagamento)
                        Toast.makeText(
                            context,
                            mensagem,
                            Toast.LENGTH_SHORT
                        ).show()

                        if (sucesso) {
                            mostrarPagamento = false
                            valorPagamento = ""
                        }
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        mostrarPagamento = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (mostrarScore) {
        AlertDialog(
            onDismissRequest = {
                mostrarScore = false
            },
            title = {
                Text("VestidoScore")
            },
            text = {
                Text(
                    "Seu VestidoScore atual é 782."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarScore = false
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
private fun ResumoFatura(
    total: Double,
    limiteTotal: Double,
    disponivel: Double,
    percentual: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Fatura atual",
                color = SecondaryText,
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                "R$ %.2f".format(total),
                color = TextColor,
                fontSize = 31.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Limite de referência",
                        color = SecondaryText,
                        fontSize = 13.sp
                    )

                    Text(
                        "R$ %.2f".format(limiteTotal),
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        "Disponível",
                        color = SecondaryText,
                        fontSize = 13.sp
                    )

                    Text(
                        "R$ %.2f".format(disponivel),
                        color = SuccessGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            LinearProgressIndicator(
                progress = {
                    percentual
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(9.dp)
                    .clip(CircleShape),
                color = VestidoPink,
                trackColor = SoftPink
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                "%.0f%% do limite utilizado".format(
                    percentual * 100
                ),
                color = SecondaryText,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun AcoesRapidas(
    onPagarFatura: () -> Unit,
    onAcessarFatura: () -> Unit,
    onVestidoScore: () -> Unit,
    onMeusLimites: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AcaoCard(
                modifier = Modifier.weight(1f),
                titulo = "Pagar fatura",
                subtitulo = "Escolher valor",
                onClick = onPagarFatura
            )

            AcaoCard(
                modifier = Modifier.weight(1f),
                titulo = "Ver movimentos",
                subtitulo = "Todos os lançamentos",
                onClick = onAcessarFatura
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AcaoCard(
                modifier = Modifier.weight(1f),
                titulo = "VestidoScore",
                subtitulo = "Acompanhar score",
                onClick = onVestidoScore
            )

            AcaoCard(
                modifier = Modifier.weight(1f),
                titulo = "Meus limites",
                subtitulo = "Consultar cartões",
                onClick = onMeusLimites
            )
        }
    }
}

@Composable
private fun AcaoCard(
    modifier: Modifier,
    titulo: String,
    subtitulo: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(96.dp)
            .clickable(
                onClick = onClick
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardWhite
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                titulo,
                color = TextColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                subtitulo,
                color = SecondaryText,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun CompraFatura(
    transacao: Transacao
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 18.dp,
                vertical = 15.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(SoftPink),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = transacao.descricao
                    .take(1)
                    .uppercase(),
                color = VestidoPink,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                transacao.descricao,
                color = TextColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                "${transacao.categoria} • ${transacao.data}",
                color = SecondaryText,
                fontSize = 12.sp
            )
        }

        Text(
            "R$ %.2f".format(transacao.valor),
            color = TextColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun LinhaData(
    titulo: String,
    valor: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            titulo,
            color = SecondaryText,
            fontSize = 14.sp
        )

        Text(
            valor,
            color = TextColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
