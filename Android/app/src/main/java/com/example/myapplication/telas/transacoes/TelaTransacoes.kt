package com.example.myapplication.telas.transacoes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.Transacao

private val VestidoPink = Color(0xFFE6007E)
private val Fundo = Color(0xFFF2F2F2)

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun TelaTransacoes(
    transacoes: List<Transacao>,
    onAdicionar: (String, Double, String, String) -> Unit,
    onRemover: (Transacao) -> Unit,
    onAbrirDetalhes: (Transacao) -> Unit
) {
    var descricao by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var data by remember { mutableStateOf("") }
    var erro by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Fundo,
        topBar = {
            TopAppBar(
                title = { Text("Movimentações") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VestidoPink,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Adicionar movimentação",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Descrição") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = valor,
                    onValueChange = { valor = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Valor") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Categoria") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = data,
                    onValueChange = { data = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Data (dd/mm/aaaa)") },
                    singleLine = true
                )

                if (erro.isNotBlank()) {
                    Text(
                        text = erro,
                        color = Color(0xFFB00020),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Button(
                    onClick = {
                        val valorNumerico = valor.replace(",", ".").toDoubleOrNull()
                        if (
                            descricao.isBlank() ||
                            categoria.isBlank() ||
                            data.isBlank() ||
                            valorNumerico == null
                        ) {
                            erro = "Preencha todos os campos corretamente."
                        } else {
                            onAdicionar(descricao, valorNumerico, categoria, data)
                            descricao = ""
                            valor = ""
                            categoria = ""
                            data = ""
                            erro = ""
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text("Adicionar")
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Histórico",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(
                items = transacoes,
                key = { it.id }
            ) { transacao ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAbrirDetalhes(transacao) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = transacao.descricao,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )
                            Text("${transacao.categoria} • ${transacao.data}")
                            Text(
                                text = "R$ %.2f".format(transacao.valor),
                                color = VestidoPink,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        IconButton(onClick = { onRemover(transacao) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remover movimentação"
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }
        }
    }
}
