package com.example.myapplication.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pix
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.TelaHome
import com.example.myapplication.model.Cartao
import com.example.myapplication.model.Transacao
import com.example.myapplication.telas.cartoes.TelaDetalheCartao
import com.example.myapplication.telas.cartoes.TelaMeusCartoes
import com.example.myapplication.telas.fatura.TelaFatura
import com.example.myapplication.telas.pix.TelaPix
import com.example.myapplication.telas.transacoes.TelaDetalheTransacao
import com.example.myapplication.telas.transacoes.TelaTransacoes

private data class ItemNavegacao(
    val rota: String,
    val titulo: String,
    val icone: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val transacoes = remember {
        mutableStateListOf(
            Transacao(1, "Mercado", 89.90, "Alimentação", "10/10/2026"),
            Transacao(2, "Netflix", 39.90, "Streaming", "08/10/2026"),
            Transacao(3, "Posto", 150.00, "Transporte", "06/10/2026")
        )
    }

    val cartoes = remember {
        mutableStateListOf(
            Cartao(1, "Cartão virtual", "4587", "Compras online", 1200.0, 340.0),
            Cartao(2, "Cartão virtual", "7214", "Assinaturas", 900.0, 210.0),
            Cartao(3, "Cartão físico", "1024", "Crédito e débito", 2500.0, 830.0)
        )
    }

    var proximoIdTransacao by remember { mutableStateOf(4) }
    var proximoIdCartao by remember { mutableStateOf(4) }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val rotaAtual = backStackEntry?.destination?.route
    val rotasPrincipais = setOf(
        Rotas.HOME,
        Rotas.FATURA,
        Rotas.TRANSACOES,
        Rotas.CARTOES,
        Rotas.PIX
    )

    Scaffold(
        bottomBar = {
            if (rotaAtual in rotasPrincipais) {
                BarraNavegacaoInferior(
                    navController = navController,
                    rotaAtual = rotaAtual
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Rotas.HOME,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Rotas.HOME) {
                TelaHome(navController)
            }

            composable(Rotas.FATURA) {
                TelaFatura(
                    onBack = { navController.popBackStack() },
                    onAcessarFatura = { navController.navigate(Rotas.TRANSACOES) },
                    onMeusLimites = { navController.navigate(Rotas.CARTOES) }
                )
            }

            composable(Rotas.TRANSACOES) {
                TelaTransacoes(
                    transacoes = transacoes,
                    onAdicionar = { descricao, valor, categoria, data ->
                        transacoes.add(
                            Transacao(
                                id = proximoIdTransacao++,
                                descricao = descricao,
                                valor = valor,
                                categoria = categoria,
                                data = data
                            )
                        )
                    },
                    onRemover = { transacoes.remove(it) },
                    onAbrirDetalhes = {
                        navController.navigate(Rotas.detalheTransacao(it.id))
                    }
                )
            }

            composable(Rotas.CARTOES) {
                TelaMeusCartoes(
                    navController = navController,
                    cartoes = cartoes,
                    onAdicionar = { nome, finalNumero, tipo, limite, usado ->
                        cartoes.add(
                            Cartao(
                                id = proximoIdCartao++,
                                nome = nome,
                                finalNumero = finalNumero,
                                tipo = tipo,
                                limite = limite,
                                usado = usado
                            )
                        )
                    },
                    onRemover = { cartoes.remove(it) },
                    onAlternarBloqueio = { cartao ->
                        val index = cartoes.indexOfFirst { it.id == cartao.id }
                        if (index >= 0) cartoes[index] = cartao.copy(bloqueado = !cartao.bloqueado)
                    },
                    onAbrirDetalhes = {
                        navController.navigate(Rotas.detalheCartao(it.id))
                    }
                )
            }

            composable(Rotas.PIX) {
                TelaPix(onBack = { navController.popBackStack() })
            }

            composable(
                route = "${Rotas.DETALHE_TRANSACAO}/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("id") ?: -1
                TelaDetalheTransacao(
                    transacao = transacoes.find { it.id == id },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = "${Rotas.DETALHE_CARTAO}/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("id") ?: -1
                TelaDetalheCartao(
                    cartao = cartoes.find { it.id == id },
                    onBack = { navController.popBackStack() },
                    onAtualizar = { atualizado ->
                        val index = cartoes.indexOfFirst { it.id == atualizado.id }
                        if (index >= 0) cartoes[index] = atualizado
                    }
                )
            }
        }
    }
}

@Composable
private fun BarraNavegacaoInferior(
    navController: NavHostController,
    rotaAtual: String?
) {
    val itens = listOf(
        ItemNavegacao(Rotas.HOME, "Home", Icons.Default.Home),
        ItemNavegacao(Rotas.FATURA, "Fatura", Icons.Default.ReceiptLong),
        ItemNavegacao(Rotas.TRANSACOES, "Movimentos", Icons.Default.SwapHoriz),
        ItemNavegacao(Rotas.CARTOES, "Cartões", Icons.Default.CreditCard),
        ItemNavegacao(Rotas.PIX, "Pix", Icons.Default.Pix)
    )

    NavigationBar {
        itens.forEach { item ->
            NavigationBarItem(
                selected = rotaAtual == item.rota,
                onClick = {
                    navController.navigate(item.rota) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icone,
                        contentDescription = item.titulo
                    )
                },
                label = { Text(item.titulo) }
            )
        }
    }
}
