package com.example.myapplication.telas.fatura

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    onBack: () -> Unit = {},
    onAcessarFatura: () -> Unit = {},
    onMeusLimites: () -> Unit = {}
) {
    val context = LocalContext.current
    var mostrarPagamento by remember { mutableStateOf(false) }
    var valorPagamento by remember { mutableStateOf("") }
    var mostrarScore by remember { mutableStateOf(false) }

    Scaffold(containerColor = Background) { innerPadding ->
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
                    .padding(start = 8.dp, top = 6.dp, end = 20.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = TextColor
                    )
                }

                Spacer(Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(SoftPink)
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Text(
                        "SETEMBRO",
                        color = VestidoPink,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    "Minha fatura",
                    color = TextColor,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(18.dp))
                ResumoFatura()
                Spacer(Modifier.height(16.dp))

                AcoesRapidas(
                    onPagarFatura = { mostrarPagamento = true },
                    onAcessarFatura = onAcessarFatura,
                    onVestidoScore = { mostrarScore = true },
                    onMeusLimites = onMeusLimites
                )

                Spacer(Modifier.height(22.dp))

                Text(
                    "Compras da fatura",
                    color = TextColor,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite)
                ) {
                    CompraFatura("Mercado Central", "Alimentação • 27 AGO", "R$ 24,90", "🛒")
                    HorizontalDivider(color = DividerColor, modifier = Modifier.padding(horizontal = 18.dp))
                    CompraFatura("Uber", "Transporte • 26 AGO", "R$ 12,40", "🚗")
                    HorizontalDivider(color = DividerColor, modifier = Modifier.padding(horizontal = 18.dp))
                    CompraFatura("Spotify", "Assinaturas • 24 AGO", "R$ 21,90", "🎵")
                    HorizontalDivider(color = DividerColor, modifier = Modifier.padding(horizontal = 18.dp))
                    CompraFatura("Farmácia", "Saúde • 22 AGO", "R$ 7,80", "💊")
                }

                Spacer(Modifier.height(18.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = SoftPink)
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Text(
                            "Limite adicional",
                            color = TextColor,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(5.dp))
                        Text(
                            "Você tem até R$ 500,00 de limite adicional para Pix e boletos no crédito.",
                            color = SecondaryText,
                            fontSize = 14.sp,
                            lineHeight = 19.sp
                        )
                    }
                }

                Spacer(Modifier.height(18.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite)
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Text(
                            "Próximas datas",
                            color = TextColor,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(14.dp))
                        LinhaData("Fechamento da fatura", "05 SET")
                        Spacer(Modifier.height(12.dp))
                        LinhaData("Vencimento", "12 SET")
                    }
                }
            }
        }
    }

    if (mostrarPagamento) {
        AlertDialog(
            onDismissRequest = { mostrarPagamento = false },
            title = { Text("Pagar fatura") },
            text = {
                Column {
                    Text("Fatura atual: R$ 67,00", fontWeight = FontWeight.Medium)
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = valorPagamento,
                        onValueChange = { valorPagamento = it },
                        label = { Text("Valor do pagamento") },
                        prefix = { Text("R$ ") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        Toast.makeText(context, "Pagamento confirmado", Toast.LENGTH_SHORT).show()
                        mostrarPagamento = false
                        valorPagamento = ""
                    }
                ) { Text("Confirmar") }
            },
            dismissButton = {
                Button(onClick = { mostrarPagamento = false }) { Text("Cancelar") }
            }
        )
    }

    if (mostrarScore) {
        AlertDialog(
            onDismissRequest = { mostrarScore = false },
            title = { Text("VestidoScore") },
            text = {
                Text("Seu VestidoScore atual é 782. Continue usando o app para acompanhar sua evolução.")
            },
            confirmButton = {
                Button(onClick = { mostrarScore = false }) { Text("OK") }
            }
        )
    }
}

@Composable
private fun ResumoFatura() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("Fatura atual", color = SecondaryText, fontSize = 14.sp)
            Spacer(Modifier.height(4.dp))
            Text(
                "R$ 67,00",
                color = TextColor,
                fontSize = 31.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text("Fecha em 05 de setembro", color = SecondaryText, fontSize = 14.sp)
            Spacer(Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Limite utilizado", color = SecondaryText, fontSize = 13.sp)
                    Text("R$ 67,00", fontWeight = FontWeight.SemiBold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Disponível", color = SecondaryText, fontSize = 13.sp)
                    Text(
                        "R$ 147,00",
                        color = SuccessGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { 0.313f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(9.dp)
                    .clip(CircleShape),
                color = VestidoPink,
                trackColor = SoftPink
            )

            Spacer(Modifier.height(8.dp))
            Text(
                "31% do seu limite foi utilizado",
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
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AcaoCard(
                Modifier.weight(1f), "💳", "Pagar fatura", "Escolher valor", onPagarFatura
            )
            AcaoCard(
                Modifier.weight(1f), "🧾", "Ver fatura", "Todos os lançamentos", onAcessarFatura
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AcaoCard(
                Modifier.weight(1f), "📈", "VestidoScore", "Acompanhar score", onVestidoScore
            )
            AcaoCard(
                Modifier.weight(1f), "⚙️", "Meus limites", "Consultar limites", onMeusLimites
            )
        }
    }
}

@Composable
private fun AcaoCard(
    modifier: Modifier,
    emoji: String,
    titulo: String,
    subtitulo: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(112.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(emoji, fontSize = 23.sp)

            Column {
                Text(
                    titulo,
                    color = TextColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    subtitulo,
                    color = SecondaryText,
                    fontSize = 11.sp,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun CompraFatura(
    titulo: String,
    categoria: String,
    valor: String,
    emoji: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(SoftPink),
            contentAlignment = Alignment.Center
        ) {
            Text(emoji, fontSize = 20.sp, textAlign = TextAlign.Center)
        }

        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Text(
                titulo,
                color = TextColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(2.dp))
            Text(categoria, color = SecondaryText, fontSize = 12.sp)
        }

        Text(
            valor,
            color = TextColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun LinhaData(titulo: String, data: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(titulo, color = SecondaryText, fontSize = 14.sp)
        Text(data, color = TextColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true, widthDp = 401, heightDp = 736)
@Composable
private fun TelaFaturaPreview() {
    TelaFatura()
}
