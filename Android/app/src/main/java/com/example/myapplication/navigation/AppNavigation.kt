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
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.cartoes.TelaDetalheCartao
import com.example.myapplication.ui.cartoes.TelaMeusCartoes
import com.example.myapplication.ui.fatura.TelaFatura
import com.example.myapplication.ui.home.TelaHome
import com.example.myapplication.ui.pix.TelaPix
import com.example.myapplication.ui.transacoes.TelaDetalheTransacao
import com.example.myapplication.ui.transacoes.TelaTransacoes

private data class ItemNavegacao(
    val rota: String,
    val titulo: String,
    val icone: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
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
                TelaHome(
                    navController = navController
                )
            }

            composable(Rotas.FATURA) {
                TelaFatura(
                    onBack = {
                        voltarParaAnteriorOuHome(
                            navController
                        )
                    },
                    onAcessarFatura = {
                        navController.navigate(
                            Rotas.TRANSACOES
                        )
                    },
                    onMeusLimites = {
                        navController.navigate(
                            Rotas.CARTOES
                        )
                    }
                )
            }

            composable(Rotas.TRANSACOES) {
                TelaTransacoes(
                    onBack = {
                        voltarParaAnteriorOuHome(
                            navController
                        )
                    },
                    onAbrirDetalhes = { transacao ->
                        navController.navigate(
                            Rotas.detalheTransacao(
                                transacao.id
                            )
                        )
                    }
                )
            }

            composable(Rotas.CARTOES) {
                TelaMeusCartoes(
                    onBack = {
                        voltarParaAnteriorOuHome(
                            navController
                        )
                    },
                    onAbrirDetalhes = { cartao ->
                        navController.navigate(
                            Rotas.detalheCartao(
                                cartao.id
                            )
                        )
                    }
                )
            }

            composable(Rotas.PIX) {
                TelaPix(
                    onBack = {
                        voltarParaAnteriorOuHome(
                            navController
                        )
                    }
                )
            }

            composable(Rotas.TRAZER_DINHEIRO) {
                val context = androidx.compose.ui.platform.LocalContext.current
                val contaRepository = com.example.myapplication.data.repository.ContaRepository.getInstance(
                    context,
                    com.example.myapplication.data.repository.TransacaoRepository(
                        com.example.myapplication.data.local.AppDatabase.getDatabase(context).transacaoDao()
                    )
                )

                com.example.myapplication.ui.trazerdinheiro.TelaTrazerDinheiro(
                    onBack = {
                        voltarParaAnteriorOuHome(
                            navController
                        )
                    },
                    onAdicionarSaldo = { valor ->
                        contaRepository.adicionarSaldo(valor)
                    }
                )
            }

            composable(
                route = "${Rotas.DETALHE_TRANSACAO}/{id}",
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.IntType
                    }
                )
            ) { entry ->
                val id = entry.arguments
                    ?.getInt("id")
                    ?: -1

                TelaDetalheTransacao(
                    id = id,
                    onBack = {
                        voltarParaAnteriorOuHome(
                            navController
                        )
                    }
                )
            }

            composable(
                route = "${Rotas.DETALHE_CARTAO}/{id}",
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.IntType
                    }
                )
            ) { entry ->
                val id = entry.arguments
                    ?.getInt("id")
                    ?: -1

                TelaDetalheCartao(
                    id = id,
                    onBack = {
                        voltarParaAnteriorOuHome(
                            navController
                        )
                    }
                )
            }
        }
    }
}

private fun voltarParaAnteriorOuHome(
    navController: NavHostController
) {
    val voltou = navController.popBackStack()

    if (!voltou) {
        navController.navigate(
            Rotas.HOME
        ) {
            popUpTo(
                Rotas.HOME
            ) {
                inclusive = false
            }

            launchSingleTop = true
        }
    }
}

private fun navegarTelaPrincipal(
    navController: NavHostController,
    rota: String
) {
    if (
        navController.currentDestination?.route == rota
    ) {
        return
    }

    if (rota == Rotas.HOME) {
        val encontrouHome = navController.popBackStack(
            route = Rotas.HOME,
            inclusive = false
        )

        if (
            !encontrouHome &&
            navController.currentDestination?.route != Rotas.HOME
        ) {
            navController.navigate(
                Rotas.HOME
            ) {
                popUpTo(
                    navController.graph.id
                ) {
                    inclusive = false
                }

                launchSingleTop = true
            }
        }

        return
    }

    navController.navigate(
        rota
    ) {
        popUpTo(
            Rotas.HOME
        ) {
            inclusive = false
        }

        launchSingleTop = true
    }
}

@Composable
private fun BarraNavegacaoInferior(
    navController: NavHostController,
    rotaAtual: String?
) {
    val itens = listOf(
        ItemNavegacao(
            Rotas.HOME,
            "Home",
            Icons.Default.Home
        ),
        ItemNavegacao(
            Rotas.FATURA,
            "Fatura",
            Icons.Default.ReceiptLong
        ),
        ItemNavegacao(
            Rotas.TRANSACOES,
            "Movimentos",
            Icons.Default.SwapHoriz
        ),
        ItemNavegacao(
            Rotas.CARTOES,
            "Cartões",
            Icons.Default.CreditCard
        ),
        ItemNavegacao(
            Rotas.PIX,
            "Pix",
            Icons.Default.Pix
        )
    )

    NavigationBar {
        itens.forEach { item ->
            NavigationBarItem(
                selected = rotaAtual == item.rota,
                onClick = {
                    navegarTelaPrincipal(
                        navController = navController,
                        rota = item.rota
                    )
                },
                icon = {
                    Icon(
                        imageVector = item.icone,
                        contentDescription = item.titulo
                    )
                },
                label = {
                    Text(
                        item.titulo
                    )
                }
            )
        }
    }
}
