package com.example.myapplication.ui.cartoes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.repository.CartaoRepository

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun TelaDetalheCartao(
    id: Int,
    onBack: () -> Unit,
    viewModel: CartoesViewModel = viewModel(
        factory = CartoesViewModelFactory(
            CartaoRepository(
                AppDatabase.getDatabase(LocalContext.current).cartaoDao()
            )
        )
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        viewModel.carregarDetalhe(id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Detalhes do cartão")
                },
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
            val cartao = uiState.cartaoDetalhe

            if (cartao == null) {
                Text("Cartão não encontrado.")
                return@Column
            }

            val disponivel = (
                cartao.limite - cartao.usado
            ).coerceAtLeast(0.0)

            val percentual = if (cartao.limite > 0) {
                (
                    cartao.usado / cartao.limite * 100
                ).coerceIn(0.0, 100.0)
            } else {
                0.0
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = cartao.nome,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "Final ${cartao.finalNumero} • ${cartao.tipo}"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Limite total: R$ %.2f".format(cartao.limite)
                    )

                    Text(
                        "Utilizado: R$ %.2f".format(cartao.usado)
                    )

                    Text(
                        text = "Disponível: R$ %.2f".format(disponivel),
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "Uso do limite: %.1f%%".format(percentual)
                    )

                    Text(
                        text = if (cartao.bloqueado) {
                            "Status: bloqueado"
                        } else {
                            "Status: ativo"
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Ajustar limite",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.novoLimite,
                onValueChange = viewModel::onNovoLimiteChange,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Novo limite")
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = viewModel::onSalvarLimite,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar novo limite")
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
