package com.example.myapplication.telas.transacoes

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
import androidx.compose.material3.Card
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
import com.example.myapplication.model.Transacao

private val VestidoPinkDetalhe = Color(0xFFE6007E)
private val FundoDetalhe = Color(0xFFF2F2F2)

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun TelaDetalheTransacao(
    transacao: Transacao?,
    onBack: () -> Unit,
    onAtualizar: (Transacao) -> Unit
) {
    var descricao by remember(transacao?.id, transacao?.descricao) {
        mutableStateOf(transacao?.descricao ?: "")
    }
    var valor by remember(transacao?.id, transacao?.valor) {
        mutableStateOf(transacao?.valor?.toString() ?: "")
    }
    var categoria by remember(transacao?.id, transacao?.categoria) {
        mutableStateOf(transacao?.categoria ?: "")
    }
    var data by remember(transacao?.id, transacao?.data) {
        mutableStateOf(transacao?.data ?: "")
    }
    var mensagem by remember { mutableStateOf("") }

    Scaffold(
        containerColor = FundoDetalhe,
        topBar = {
            TopAppBar(
                title = { Text("Detalhes da movimentação", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VestidoPinkDetalhe
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
            if (transacao == null) {
                Text("Carregando movimentação do banco...")
                return@Column
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Movimentação #${transacao.id}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Edite os campos abaixo e salve para atualizar no Room.")
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Descrição") },
                singleLine = true,
                colors = coresCampoDetalhe()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = valor,
                onValueChange = { valor = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Valor") },
                prefix = { Text("R$ ") },
                singleLine = true,
                colors = coresCampoDetalhe()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Categoria") },
                singleLine = true,
                colors = coresCampoDetalhe()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = data,
                onValueChange = { data = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Data") },
                singleLine = true,
                colors = coresCampoDetalhe()
            )
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    val valorNumerico = valor.replace(",", ".").toDoubleOrNull()
                    if (
                        descricao.isNotBlank() &&
                        categoria.isNotBlank() &&
                        data.isNotBlank() &&
                        valorNumerico != null &&
                        valorNumerico > 0
                    ) {
                        onAtualizar(
                            transacao.copy(
                                descricao = descricao,
                                valor = valorNumerico,
                                categoria = categoria,
                                data = data
                            )
                        )
                        mensagem = "Movimentação atualizada no banco."
                    } else {
                        mensagem = "Preencha todos os campos corretamente."
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VestidoPinkDetalhe,
                    contentColor = Color.White
                )
            ) {
                Text("Salvar alterações", fontWeight = FontWeight.Bold)
            }

            if (mensagem.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(mensagem)
            }
        }
    }
}

@Composable
private fun coresCampoDetalhe() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = VestidoPinkDetalhe,
    focusedLabelColor = VestidoPinkDetalhe,
    cursorColor = VestidoPinkDetalhe,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White
)
