package com.example.myapplication.ui.trazerdinheiro

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val VestidoPink = Color(0xFFE6007E)
private val Fundo = Color(0xFFF2F2F2)
private val TextoEscuro = Color(0xFF202020)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaTrazerDinheiro(
    onBack: () -> Unit,
    onAdicionarSaldo: (Double) -> Unit = {}
) {
    var valor by remember {
        mutableStateOf("")
    }

    var mensagem by remember {
        mutableStateOf("")
    }

    var sucesso by remember {
        mutableStateOf(false)
    }

    Scaffold(
        containerColor = Fundo,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Trazer Dinheiro",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VestidoPink
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
        ) {
            Text(
                text = "Depositar na Conta",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextoEscuro
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Informe o valor em reais que deseja adicionar ao seu saldo.",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            OutlinedTextField(
                value = valor,
                onValueChange = {
                    valor = it
                    mensagem = ""
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Valor")
                },
                singleLine = true,
                prefix = {
                    Text("R$ ")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VestidoPink,
                    focusedLabelColor = VestidoPink,
                    cursorColor = VestidoPink
                )
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Button(
                onClick = {
                    val valorNumerico = valor
                        .replace(",", ".")
                        .toDoubleOrNull()

                    if (valorNumerico != null && valorNumerico > 0) {
                        onAdicionarSaldo(valorNumerico)
                        mensagem = "R$ %.2f adicionado ao seu saldo com sucesso!".format(valorNumerico)
                        sucesso = true
                        valor = ""
                    } else {
                        mensagem = "Informe um valor válido maior que zero."
                        sucesso = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VestidoPink,
                    contentColor = Color.White
                )
            ) {
                Text(
                    "Confirmar",
                    fontWeight = FontWeight.Bold
                )
            }

            if (mensagem.isNotBlank()) {
                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Text(
                    text = mensagem,
                    color = if (sucesso) {
                        VestidoPink
                    } else {
                        Color(0xFFB00020)
                    },
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun PopUpTrazerDinheiro(
    onDismiss: () -> Unit,
    onConfirmar: (Double) -> Unit
) {
    var valorInput by remember {
        mutableStateOf("")
    }

    var erroInput by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Trazer dinheiro",
                fontWeight = FontWeight.Bold,
                color = VestidoPink
            )
        },
        text = {
            Column {
                Text(
                    text = "Informe o valor em reais que deseja adicionar à sua conta:",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                OutlinedTextField(
                    value = valorInput,
                    onValueChange = {
                        valorInput = it
                        erroInput = ""
                    },
                    label = {
                        Text("Valor")
                    },
                    prefix = {
                        Text("R$ ")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VestidoPink,
                        focusedLabelColor = VestidoPink,
                        cursorColor = VestidoPink
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                if (erroInput.isNotBlank()) {
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = erroInput,
                        color = Color(0xFFB00020),
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val valorNumerico = valorInput
                        .replace(",", ".")
                        .toDoubleOrNull()

                    if (valorNumerico != null && valorNumerico > 0) {
                        onConfirmar(valorNumerico)
                        onDismiss()
                    } else {
                        erroInput = "Informe um valor válido maior que zero."
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = VestidoPink,
                    contentColor = Color.White
                )
            ) {
                Text(
                    "Adicionar",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    "Cancelar",
                    color = Color.Gray
                )
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}
