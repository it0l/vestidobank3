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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun TelaPix(onBack: () -> Unit) {
    var chave by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    var mensagem by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pix") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
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
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = chave,
                onValueChange = { chave = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Chave Pix") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = valor,
                onValueChange = { valor = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Valor") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {
                    val valorNumerico = valor.replace(",", ".").toDoubleOrNull()
                    mensagem = if (chave.isNotBlank() && valorNumerico != null && valorNumerico > 0) {
                        val texto = "Pix de R$ %.2f preparado para %s".format(valorNumerico, chave)
                        chave = ""
                        valor = ""
                        texto
                    } else {
                        "Informe uma chave e um valor válido."
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continuar")
            }

            if (mensagem.isNotBlank()) {
                Spacer(modifier = Modifier.height(18.dp))
                Text(mensagem)
            }
        }
    }
}
