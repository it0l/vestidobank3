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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.Transacao

private val VestidoPink = Color(0xFFE6007E)
private val VestidoPinkLight = Color(0xFFFFE8F3)
private val Fundo = Color(0xFFF2F2F2)
private val TextoEscuro = Color(0xFF202020)

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun TelaTransacoes(
    transacoes: List<Transacao>,
    onAdicionar: (String, Double, String, String) -> Unit,
    onRemover: (Transacao) -> Unit,
    onAbrirDetalhes: (Transacao) -> Unit
) {

    var descricao by remember {
        mutableStateOf("")
    }

    var valor by remember {
        mutableStateOf("")
    }

    var categoria by remember {
        mutableStateOf("")
    }

    var data by remember {
        mutableStateOf("")
    }

    var erro by remember {
        mutableStateOf("")
    }

    Scaffold(
        containerColor = Fundo,

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Movimentações",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },

                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = VestidoPink
                    )
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            item {

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = "Adicionar movimentação",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoEscuro
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "Cadastre uma nova movimentação na conta.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                OutlinedTextField(
                    value = descricao,

                    onValueChange = {
                        descricao = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text("Descrição")
                    },

                    singleLine = true,

                    colors =
                        coresCampo()
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = valor,

                    onValueChange = {
                        valor = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text("Valor")
                    },

                    prefix = {
                        Text("R$ ")
                    },

                    singleLine = true,

                    colors =
                        coresCampo()
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = categoria,

                    onValueChange = {
                        categoria = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text("Categoria")
                    },

                    singleLine = true,

                    colors =
                        coresCampo()
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = data,

                    onValueChange = {
                        data = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text("Data (dd/mm/aaaa)")
                    },

                    singleLine = true,

                    colors =
                        coresCampo()
                )

                if (erro.isNotBlank()) {

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text = erro,
                        color = Color(0xFFB00020)
                    )
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Button(
                    onClick = {

                        val valorNumerico =
                            valor
                                .replace(",", ".")
                                .toDoubleOrNull()

                        if (
                            descricao.isBlank() ||
                            categoria.isBlank() ||
                            data.isBlank() ||
                            valorNumerico == null ||
                            valorNumerico <= 0
                        ) {

                            erro =
                                "Preencha todos os campos corretamente."

                        } else {

                            onAdicionar(
                                descricao,
                                valorNumerico,
                                categoria,
                                data
                            )

                            descricao = ""
                            valor = ""
                            categoria = ""
                            data = ""
                            erro = ""
                        }
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = VestidoPink,
                            contentColor = Color.White
                        )
                ) {

                    Text(
                        text = "Adicionar movimentação",
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(
                    modifier = Modifier.height(22.dp)
                )

                Text(
                    text = "Histórico",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoEscuro
                )

                Text(
                    text = "${transacoes.size} movimentação(ões)",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            items(
                items = transacoes,
                key = {
                    it.id
                }
            ) { transacao ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onAbrirDetalhes(
                                transacao
                            )
                        },

                    shape =
                        RoundedCornerShape(16.dp),

                    colors =
                        CardDefaults.cardColors(
                            containerColor = Color.White
                        ),

                    elevation =
                        CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),

                        horizontalArrangement =
                            Arrangement.SpaceBetween,

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Column(
                            modifier =
                                Modifier.weight(1f)
                        ) {

                            Text(
                                text =
                                    transacao.descricao,

                                fontWeight =
                                    FontWeight.Bold,

                                fontSize = 17.sp,

                                color = TextoEscuro
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(3.dp)
                            )

                            Text(
                                text =
                                    "${transacao.categoria} • ${transacao.data}",

                                color =
                                    Color.Gray,

                                fontSize =
                                    13.sp
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(7.dp)
                            )

                            Text(
                                text =
                                    "R$ %.2f"
                                        .format(
                                            transacao.valor
                                        ),

                                color =
                                    VestidoPink,

                                fontWeight =
                                    FontWeight.Bold,

                                fontSize =
                                    17.sp
                            )
                        }

                        IconButton(
                            onClick = {
                                onRemover(
                                    transacao
                                )
                            }
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.Delete,

                                contentDescription =
                                    "Remover movimentação",

                                tint =
                                    VestidoPink
                            )
                        }
                    }
                }
            }

            item {

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )
            }
        }
    }
}

@Composable
private fun coresCampo() =
    OutlinedTextFieldDefaults.colors(

        focusedBorderColor =
            VestidoPink,

        focusedLabelColor =
            VestidoPink,

        cursorColor =
            VestidoPink,

        focusedContainerColor =
            Color.White,

        unfocusedContainerColor =
            Color.White
    )