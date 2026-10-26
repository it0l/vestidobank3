package com.example.myapplication.telas.pix

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val VestidoPink = Color(0xFFE6007E)
private val Fundo = Color(0xFFF2F2F2)
private val TextoEscuro = Color(0xFF202020)

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun TelaPix(onBack: () -> Unit) {
    var chave by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    var mensagem by remember { mutableStateOf("") }
    var sucesso by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Fundo,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pix",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VestidoPink)
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
                text = "Enviar Pix",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextoEscuro
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Informe a chave e o valor da transferência.",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = chave,
                onValueChange = { chave = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Chave Pix") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VestidoPink,
                    focusedLabelColor = VestidoPink,
                    cursorColor = VestidoPink
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = valor,
                onValueChange = { valor = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Valor") },
                singleLine = true,
                prefix = { Text("R$ ") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VestidoPink,
                    focusedLabelColor = VestidoPink,
                    cursorColor = VestidoPink
                )
            )
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val valorNumerico = valor.replace(",", ".").toDoubleOrNull()
                    if (chave.isNotBlank() && valorNumerico != null && valorNumerico > 0) {
                        mensagem = "Pix de R$ %.2f preparado para %s".format(valorNumerico, chave)
                        sucesso = true
                        chave = ""
                        valor = ""
                    } else {
                        mensagem = "Informe uma chave e um valor válido."
                        sucesso = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VestidoPink,
                    contentColor = Color.White
                )
            ) {
                Text("Continuar", fontWeight = FontWeight.Bold)
            }

            if (mensagem.isNotBlank()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = mensagem,
                    color = if (sucesso) VestidoPink else Color(0xFFB00020),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
