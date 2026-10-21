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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.TelaHome
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.model.Cartao
import com.example.myapplication.model.Transacao
import com.example.myapplication.telas.cartoes.TelaDetalheCartao
import com.example.myapplication.telas.cartoes.TelaMeusCartoes
import com.example.myapplication.telas.fatura.TelaFatura
import com.example.myapplication.telas.pix.TelaPix
import com.example.myapplication.telas.transacoes.TelaDetalheTransacao
import com.example.myapplication.telas.transacoes.TelaTransacoes
import kotlinx.coroutines.launch

private data class ItemNavegacao(
    val rota: String,
    val titulo: String,
    val icone: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun AppNavigation() {

    val navController = rememberNavController()


    val context = LocalContext.current

    val database = remember {
        AppDatabase.getDatabase(context)
    }

    val transacaoDao = database.transacaoDao()
    val cartaoDao = database.cartaoDao()


    val transacoes by transacaoDao
        .listarTodas()
        .collectAsState(initial = emptyList())

    val cartoes by cartaoDao
        .listarTodos()
        .collectAsState(initial = emptyList())

    val scope = rememberCoroutineScope()


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

            /*
             * HOME
             */
            composable(Rotas.HOME) {

                TelaHome(navController)
            }

            /*
             * FATURA
             */
            composable(Rotas.FATURA) {

                TelaFatura(

                    onBack = {
                        navController.popBackStack()
                    },

                    onAcessarFatura = {
                        navController.navigate(Rotas.TRANSACOES)
                    },

                    onMeusLimites = {
                        navController.navigate(Rotas.CARTOES)
                    }
                )
            }

            /*
             * TRANSAÇÕES
             */
            composable(Rotas.TRANSACOES) {

                TelaTransacoes(

                    transacoes = transacoes,

                    onAdicionar = {
                            descricao,
                            valor,
                            categoria,
                            data ->

                        scope.launch {

                            transacaoDao.inserir(

                                Transacao(
                                    descricao = descricao,
                                    valor = valor,
                                    categoria = categoria,
                                    data = data
                                )
                            )
                        }
                    },

                    onRemover = { transacao ->

                        scope.launch {

                            transacaoDao.remover(
                                transacao
                            )
                        }
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

            /*
             * CARTÕES
             */
            composable(Rotas.CARTOES) {

                TelaMeusCartoes(

                    navController = navController,

                    cartoes = cartoes,

                    onAdicionar = {
                            nome,
                            finalNumero,
                            tipo,
                            limite,
                            usado ->

                        scope.launch {

                            cartaoDao.inserir(

                                Cartao(
                                    nome = nome,
                                    finalNumero = finalNumero,
                                    tipo = tipo,
                                    limite = limite,
                                    usado = usado
                                )
                            )
                        }
                    },

                    onRemover = { cartao ->

                        scope.launch {

                            cartaoDao.remover(
                                cartao
                            )
                        }
                    },

                    onAlternarBloqueio = { cartao ->

                        scope.launch {

                            cartaoDao.atualizar(

                                cartao.copy(
                                    bloqueado =
                                        !cartao.bloqueado
                                )
                            )
                        }
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

            /*
             * PIX
             */
            composable(Rotas.PIX) {

                TelaPix(

                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            /*
             * DETALHE DA TRANSAÇÃO
             */
            composable(

                route =
                    "${Rotas.DETALHE_TRANSACAO}/{id}",

                arguments = listOf(

                    navArgument("id") {
                        type = NavType.IntType
                    }
                )

            ) { entry ->

                val id =
                    entry.arguments
                        ?.getInt("id")
                        ?: -1

                val transacao =
                    transacoes.find {
                        it.id == id
                    }

                TelaDetalheTransacao(

                    transacao = transacao,

                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            /*
             * DETALHE DO CARTÃO
             */
            composable(

                route =
                    "${Rotas.DETALHE_CARTAO}/{id}",

                arguments = listOf(

                    navArgument("id") {
                        type = NavType.IntType
                    }
                )

            ) { entry ->

                val id =
                    entry.arguments
                        ?.getInt("id")
                        ?: -1

                val cartao =
                    cartoes.find {
                        it.id == id
                    }

                TelaDetalheCartao(

                    cartao = cartao,

                    onBack = {
                        navController.popBackStack()
                    },

                    onAtualizar = { atualizado ->

                        scope.launch {

                            cartaoDao.atualizar(
                                atualizado
                            )
                        }
                    }
                )
            }
        }
    }
}

/*
 * BARRA DE NAVEGAÇÃO INFERIOR
 */
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

                selected =
                    rotaAtual == item.rota,

                onClick = {

                    navController.navigate(
                        item.rota
                    ) {

                        popUpTo(
                            navController.graph
                                .findStartDestination()
                                .id
                        ) {

                            saveState = true
                        }

                        launchSingleTop = true

                        restoreState = true
                    }
                },

                icon = {

                    Icon(
                        imageVector = item.icone,
                        contentDescription =
                            item.titulo
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