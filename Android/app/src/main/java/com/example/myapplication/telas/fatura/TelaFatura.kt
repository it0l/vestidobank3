package com.example.myapplication.telas.fatura

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Background = Color(0xFFF0F1F5)
private val VestidoPink = Color(0xFFE31A6B)
private val PromoBackground = Color(0xFFF3DEDE)
private val LimitGreen = Color(0xFF51FF00)
private val LimitRed = Color(0xFFE31919)
private val TextColor = Color(0xFF050505)

@Composable
fun TelaFatura(
    onBack: () -> Unit = {},
    onAcessarFatura: () -> Unit = {},
    onMeusLimites: () -> Unit = {}
) {
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
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .padding(start = 13.dp, top = 10.dp)
                        .size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = TextColor,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(434.dp)
            ) {

                Column(
                    modifier = Modifier
                        .padding(
                            start = 40.dp,
                            top = 109.dp
                        )
                ) {

                    Text(
                        text = "Fatura atual",
                        color = TextColor,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "R$67,00",
                        color = TextColor,
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Limite disponível R$147,00",
                        color = TextColor,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Fecha em 05 de SET",
                        color = TextColor,
                        fontSize = 15.sp
                    )
                }

                LimitBar(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(
                            top = 10.dp,
                            end = 35.dp
                        )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            QuickActions(
                onPagarFatura = { mostrarPagamento = true },
                onAcessarFatura = onAcessarFatura,
                onVestidoScore = { mostrarScore = true },
                onMeusLimites = onMeusLimites
            )

            Spacer(modifier = Modifier.height(18.dp))

            PromoBanner()

            Spacer(modifier = Modifier.height(19.dp))
        }
    }

    if (mostrarPagamento) {
        AlertDialog(
            onDismissRequest = {
                mostrarPagamento = false
            },
            title = {
                Text(text = "Pagar fatura")
            },
            text = {
                OutlinedTextField(
                    value = valorPagamento,
                    onValueChange = {
                        valorPagamento = it
                    },
                    label = {
                        Text(text = "Valor")
                    },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        Toast.makeText(
                            context,
                            "Pagamento confirmado",
                            Toast.LENGTH_SHORT
                        ).show()

                        mostrarPagamento = false
                        valorPagamento = ""
                    }
                ) {
                    Text(text = "Confirmar")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        mostrarPagamento = false
                    }
                ) {
                    Text(text = "Cancelar")
                }
            }
        )
    }

    if (mostrarScore) {
        AlertDialog(
            onDismissRequest = { mostrarScore = false },
            title = { Text("VestidoScore") },
            text = { Text("Seu VestidoScore atual é 782. Continue usando o app para acompanhar sua evolução.") },
            confirmButton = {
                Button(onClick = { mostrarScore = false }) {
                    Text("OK")
                }
            }
        )
    }

}

@Composable
private fun LimitBar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(36.dp)
            .height(409.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(LimitGreen)
    ) {

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(36.dp)
                .height(196.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(LimitRed)
        )
    }
}

@Composable
private fun QuickActions(
    onPagarFatura: () -> Unit,
    onAcessarFatura: () -> Unit,
    onVestidoScore: () -> Unit,
    onMeusLimites: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(102.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top
    ) {

        QuickAction(
            text = "Pagar fatura",
            onClick = onPagarFatura
        )

        QuickAction(
            text = "Acessar\nfatura",
            onClick = onAcessarFatura
        )

        QuickAction(
            text = "VestidoScore",
            onClick = onVestidoScore
        )

        QuickAction(
            text = "Meus limites",
            onClick = onMeusLimites
        )
    }
}

@Composable
private fun QuickAction(
    text: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.width(92.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = onClick,
            modifier = Modifier.size(59.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = VestidoPink),
            contentPadding = PaddingValues(0.dp),
            elevation = null
        ) {
        }

        Spacer(modifier = Modifier.height(11.dp))

        Text(
            text = text,
            color = TextColor,
            fontSize = 14.sp,
            lineHeight = 16.sp,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

@Composable
private fun PromoBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(88.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(PromoBackground)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        MoneyBagIcon()

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = buildAnnotatedString {

                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("R$500,00 de limite adicional")
                }

                append(" para\n")
                append("Pix e boletos no crédito")
            },
            color = TextColor,
            fontSize = 16.sp,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun MoneyBagIcon() {
    Box(
        modifier = Modifier.size(44.dp),
        contentAlignment = Alignment.Center
    ) {

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {

            val w = size.width
            val h = size.height

            val stroke = Stroke(
                width = 2.2.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )

            val bag = Path().apply {

                moveTo(w * 0.36f, h * 0.17f)

                lineTo(w * 0.64f, h * 0.17f)

                lineTo(w * 0.58f, h * 0.29f)

                quadraticBezierTo(
                    w * 0.78f,
                    h * 0.42f,
                    w * 0.82f,
                    h * 0.68f
                )

                quadraticBezierTo(
                    w * 0.84f,
                    h * 0.88f,
                    w * 0.64f,
                    h * 0.90f
                )

                lineTo(
                    w * 0.36f,
                    h * 0.90f
                )

                quadraticBezierTo(
                    w * 0.16f,
                    h * 0.88f,
                    w * 0.18f,
                    h * 0.68f
                )

                quadraticBezierTo(
                    w * 0.22f,
                    h * 0.42f,
                    w * 0.42f,
                    h * 0.29f
                )

                close()
            }

            drawPath(
                path = bag,
                color = TextColor,
                style = stroke
            )

            drawLine(
                color = TextColor,
                start = Offset(
                    w * 0.34f,
                    h * 0.31f
                ),
                end = Offset(
                    w * 0.66f,
                    h * 0.31f
                ),
                strokeWidth = 2.2.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        Text(
            text = "$",
            color = TextColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.offset(y = 6.dp)
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 401,
    heightDp = 736
)
@Composable
private fun TelaFaturaPreview() {
    TelaFatura()
}
