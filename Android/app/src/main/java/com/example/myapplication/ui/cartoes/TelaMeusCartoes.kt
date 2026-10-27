package com.example.myapplication.ui.cartoes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.Cartao
import com.example.myapplication.data.repository.CartaoRepository

private val Background = Color(0xFFF0F1F5)
private val VestidoPink = Color(0xFFE31A6B)
private val TextColor = Color(0xFF050505)

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun TelaMeusCartoes(
    onAbrirDetalhes: (Cartao) -> Unit,
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

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Text("Meus cartões")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VestidoPink,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
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
                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    PinkChip(text = "Cartãozinho")
                    Spacer(modifier = Modifier.width(12.dp))
                    PinkChip(text = "Adicionais")
                }

                Spacer(modifier = Modifier.height(22.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ActionItem(
                        modifier = Modifier.weight(1f),
                        iconRes = R.drawable.ic_vestido_mais,
                        title = "Criar cartão\nvirtual",
                        iconSize = 30.dp,
                        onClick = viewModel::onAlternarFormulario
                    )

                    ActionItem(
                        modifier = Modifier.weight(1f),
                        iconRes = R.drawable.ic_vestido_cadeado,
                        title = "Bloquear\ncartões",
                        iconSize = 29.dp,
                        onClick = viewModel::onAlternarModoBloqueio
                    )

                    ActionItem(
                        modifier = Modifier.weight(1f),
                        iconRes = R.drawable.ic_vestido_carteira,
                        title = "Adicionar à\ncarteira",
                        iconSize = 30.dp,
                        onClick = viewModel::onCarteira
                    )

                    ActionItem(
                        modifier = Modifier.weight(1f),
                        iconRes = R.drawable.ic_vestido_ajustes,
                        title = "Ajustar limites",
                        iconSize = 30.dp,
                        onClick = {
                            uiState.cartoes.firstOrNull()?.let(
                                onAbrirDetalhes
                            )
                        }
                    )
                }

                if (uiState.mensagemAcao.isNotBlank()) {
                    Text(
                        text = uiState.mensagemAcao,
                        modifier = Modifier.padding(top = 12.dp),
                        color = Color.DarkGray
                    )
                }

                if (uiState.mostrarFormulario) {
                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Novo cartão",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = uiState.nome,
                        onValueChange = viewModel::onNomeChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Nome do cartão") },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = uiState.finalNumero,
                        onValueChange = viewModel::onFinalNumeroChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("4 últimos números") },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = uiState.tipo,
                        onValueChange = viewModel::onTipoChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Tipo/uso") },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = uiState.limite,
                        onValueChange = viewModel::onLimiteChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Limite") },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = uiState.usado,
                        onValueChange = viewModel::onUsadoChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Valor usado") },
                        singleLine = true
                    )

                    if (uiState.erro.isNotBlank()) {
                        Text(
                            text = uiState.erro,
                            color = Color(0xFFB00020),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Button(
                        onClick = viewModel::onAdicionar,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text("Adicionar cartão")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Cartões cadastrados (${uiState.cartoes.size})",
                    color = TextColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(
                items = uiState.cartoes,
                key = { it.id }
            ) { cartao ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onAbrirDetalhes(cartao)
                        },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = VestidoPink
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.ic_vestido_cartao
                            ),
                            contentDescription = "Cartão",
                            tint = Color.White,
                            modifier = Modifier.size(42.dp)
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = cartao.nome,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )

                            Text(
                                text = "•••• ${cartao.finalNumero} • ${cartao.tipo}",
                                color = Color.White.copy(alpha = 0.9f)
                            )

                            Text(
                                text = if (cartao.bloqueado) {
                                    "BLOQUEADO"
                                } else {
                                    "Ativo"
                                },
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (uiState.modoBloqueio) {
                            Checkbox(
                                checked = cartao.bloqueado,
                                onCheckedChange = {
                                    viewModel.onAlternarBloqueio(cartao)
                                }
                            )
                        }

                        IconButton(
                            onClick = {
                                viewModel.onDeletar(cartao)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remover cartão",
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}

@Composable
private fun PinkChip(text: String) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = VestidoPink
        )
    ) {
        Text(
            text = text,
            color = Color.White,
            modifier = Modifier.padding(
                horizontal = 18.dp,
                vertical = 8.dp
            )
        )
    }
}

@Composable
private fun ActionItem(
    modifier: Modifier = Modifier,
    iconRes: Int,
    title: String,
    iconSize: Dp,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.size(56.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = VestidoPink
            ),
            contentPadding = PaddingValues(0.dp),
            elevation = null
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                tint = TextColor,
                modifier = Modifier.size(iconSize)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = title,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            lineHeight = 14.sp,
            maxLines = 2
        )
    }
}
