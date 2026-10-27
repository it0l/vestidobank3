package com.example.myapplication.ui.transacoes

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.repository.TransacaoRepository

private val VestidoPinkDetalhe = Color(0xFFE6007E)
private val FundoDetalhe = Color(0xFFF2F2F2)

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun TelaDetalheTransacao(
    id: Int,
    onBack: () -> Unit,
    viewModel: TransacoesViewModel = viewModel(
        factory = TransacoesViewModelFactory(
            TransacaoRepository(
                AppDatabase.getDatabase(LocalContext.current).transacaoDao()
            )
        )
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        viewModel.carregarDetalhe(id)
    }

    Scaffold(
        containerColor = FundoDetalhe,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detalhes da movimentação",
                        color = Color.White
                    )
                },
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
            val transacao = uiState.transacaoDetalhe

            if (transacao == null) {
                Text("Carregando movimentação do banco...")
                return@Column
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Movimentação #${transacao.id}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        "Edite os campos abaixo e salve para atualizar no Room."
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            OutlinedTextField(
                value = uiState.detalheDescricao,
                onValueChange = viewModel::onDetalheDescricaoChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Descrição") },
                singleLine = true,
                colors = coresCampoDetalhe()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.detalheValor,
                onValueChange = viewModel::onDetalheValorChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Valor") },
                prefix = { Text("R$ ") },
                singleLine = true,
                colors = coresCampoDetalhe()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.detalheCategoria,
                onValueChange = viewModel::onDetalheCategoriaChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Categoria") },
                singleLine = true,
                colors = coresCampoDetalhe()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.detalheData,
                onValueChange = viewModel::onDetalheDataChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Data") },
                singleLine = true,
                colors = coresCampoDetalhe()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = viewModel::onSalvarDetalhe,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VestidoPinkDetalhe,
                    contentColor = Color.White
                )
            ) {
                Text(
                    "Salvar alterações",
                    fontWeight = FontWeight.Bold
                )
            }

            if (uiState.mensagemDetalhe.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    uiState.mensagemDetalhe
                )
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
