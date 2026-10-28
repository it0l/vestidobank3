package com.example.myapplication.ui.pix

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.repository.ContaRepository
import com.example.myapplication.data.repository.TransacaoRepository

private val VestidoPink = Color(0xFFE6007E)
private val Fundo = Color(0xFFF2F2F2)
private val TextoEscuro = Color(0xFF202020)

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun TelaPix(
    onBack: () -> Unit,
    viewModel: PixViewModel = viewModel(
        factory = PixViewModelFactory(
            ContaRepository.getInstance(
                LocalContext.current,
                TransacaoRepository(
                    AppDatabase.getDatabase(LocalContext.current).transacaoDao()
                )
            )
        )
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val saldoFormatado = "R$ %.2f".format(uiState.saldoAtual).replace('.', ',')

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
                text = "Enviar Pix",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextoEscuro
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Informe a chave e o valor da transferência.",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Saldo disponível: $saldoFormatado",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = VestidoPink
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            OutlinedTextField(
                value = uiState.chave,
                onValueChange = viewModel::onChaveChange,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Chave Pix")
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VestidoPink,
                    focusedLabelColor = VestidoPink,
                    cursorColor = VestidoPink
                )
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            OutlinedTextField(
                value = uiState.valor,
                onValueChange = viewModel::onValorChange,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Valor")
                },
                singleLine = true,
                prefix = {
                    Text("R$ ")
                },
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
                onClick = viewModel::onEnviarPix,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VestidoPink,
                    contentColor = Color.White
                )
            ) {
                Text(
                    "Continuar",
                    fontWeight = FontWeight.Bold
                )
            }

            if (uiState.mensagem.isNotBlank()) {
                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Text(
                    text = uiState.mensagem,
                    color = if (uiState.sucesso) {
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
