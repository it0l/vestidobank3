package com.example.myapplication.navigation

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG_BANCO = "VestidoBankDB"

private data class ItemNavegacao(
    val rota: String,
    val titulo: String,
    val icone: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val transacaoDao = remember(database) { database.transacaoDao() }
    val cartaoDao = remember(database) { database.cartaoDao() }

    var transacoes by remember { mutableStateOf<List<Transacao>>(emptyList()) }
    var cartoes by remember { mutableStateOf<List<Cartao>>(emptyList()) }

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
                    onAcessarFatura = {
                        navController.navigate(Rotas.TRANSACOES)
                    },
                    onMeusLimites = {
                        navController.navigate(Rotas.CARTOES)
                    }
                )
            }

            composable(Rotas.TRANSACOES) {
                LaunchedEffect(Unit) {
                    transacoes = withContext(Dispatchers.IO) {
                        try {
                            transacaoDao.buscarTodos()
                        } catch (e: Exception) {
                            Log.e(TAG_BANCO, "Erro ao buscar transações", e)
                            emptyList()
                        }
                    }
                }

                TelaTransacoes(
                    transacoes = transacoes,
                    onBack = {
                        navController.popBackStack()
                    },
                    onAdicionar = { descricao, valor, categoria, data ->
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                transacaoDao.inserir(
                                    Transacao(
                                        descricao = descricao,
                                        valor = valor,
                                        categoria = categoria,
                                        data = data
                                    )
                                )

                                val atualizadas = transacaoDao.buscarTodos()

                                withContext(Dispatchers.Main) {
                                    transacoes = atualizadas
                                }
                            } catch (e: Exception) {
                                Log.e(
                                    TAG_BANCO,
                                    "Erro ao inserir transação",
                                    e
                                )
                            }
                        }
                    },
                    onRemover = { transacao ->
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                transacaoDao.deletar(transacao)
                                val atualizadas = transacaoDao.buscarTodos()

                                withContext(Dispatchers.Main) {
                                    transacoes = atualizadas
                                }
                            } catch (e: Exception) {
                                Log.e(
                                    TAG_BANCO,
                                    "Erro ao remover transação",
                                    e
                                )
                            }
                        }
                    },
                    onAbrirDetalhes = { transacao ->
                        navController.navigate(
                            Rotas.detalheTransacao(transacao.id)
                        )
                    }
                )
            }

            composable(Rotas.CARTOES) {
                LaunchedEffect(Unit) {
                    cartoes = withContext(Dispatchers.IO) {
                        try {
                            cartaoDao.buscarTodos()
                        } catch (e: Exception) {
                            Log.e(TAG_BANCO, "Erro ao buscar cartões", e)
                            emptyList()
                        }
                    }
                }

                TelaMeusCartoes(
                    navController = navController,
                    cartoes = cartoes,
                    onAdicionar = { nome, finalNumero, tipo, limite, usado ->
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                cartaoDao.inserir(
                                    Cartao(
                                        nome = nome,
                                        finalNumero = finalNumero,
                                        tipo = tipo,
                                        limite = limite,
                                        usado = usado
                                    )
                                )

                                val atualizados = cartaoDao.buscarTodos()

                                withContext(Dispatchers.Main) {
                                    cartoes = atualizados
                                }
                            } catch (e: Exception) {
                                Log.e(
                                    TAG_BANCO,
                                    "Erro ao inserir cartão",
                                    e
                                )
                            }
                        }
                    },
                    onRemover = { cartao ->
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                cartaoDao.deletar(cartao)
                                val atualizados = cartaoDao.buscarTodos()

                                withContext(Dispatchers.Main) {
                                    cartoes = atualizados
                                }
                            } catch (e: Exception) {
                                Log.e(
                                    TAG_BANCO,
                                    "Erro ao remover cartão",
                                    e
                                )
                            }
                        }
                    },
                    onAlternarBloqueio = { cartao ->
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                cartaoDao.atualizar(
                                    cartao.copy(
                                        bloqueado = !cartao.bloqueado
                                    )
                                )

                                val atualizados = cartaoDao.buscarTodos()

                                withContext(Dispatchers.Main) {
                                    cartoes = atualizados
                                }
                            } catch (e: Exception) {
                                Log.e(
                                    TAG_BANCO,
                                    "Erro ao atualizar bloqueio do cartão",
                                    e
                                )
                            }
                        }
                    },
                    onAbrirDetalhes = { cartao ->
                        navController.navigate(
                            Rotas.detalheCartao(cartao.id)
                        )
                    }
                )
            }

            composable(Rotas.PIX) {
                TelaPix(
                    onBack = {
                        navController.popBackStack()
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
                val id = entry.arguments?.getInt("id") ?: -1
                var transacaoDetalhe by remember(id) {
                    mutableStateOf<Transacao?>(null)
                }

                LaunchedEffect(id) {
                    transacaoDetalhe = withContext(Dispatchers.IO) {
                        try {
                            transacaoDao.buscarPorId(id)
                        } catch (e: Exception) {
                            Log.e(
                                TAG_BANCO,
                                "Erro ao buscar transação por id",
                                e
                            )
                            null
                        }
                    }
                }

                TelaDetalheTransacao(
                    transacao = transacaoDetalhe,
                    onBack = {
                        navController.popBackStack()
                    },
                    onAtualizar = { atualizado ->
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                transacaoDao.atualizar(atualizado)

                                val recarregada =
                                    transacaoDao.buscarPorId(id)

                                val listaAtualizada =
                                    transacaoDao.buscarTodos()

                                withContext(Dispatchers.Main) {
                                    transacaoDetalhe = recarregada
                                    transacoes = listaAtualizada
                                }
                            } catch (e: Exception) {
                                Log.e(
                                    TAG_BANCO,
                                    "Erro ao atualizar transação",
                                    e
                                )
                            }
                        }
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
                val id = entry.arguments?.getInt("id") ?: -1
                var cartaoDetalhe by remember(id) {
                    mutableStateOf<Cartao?>(null)
                }

                LaunchedEffect(id) {
                    cartaoDetalhe = withContext(Dispatchers.IO) {
                        try {
                            cartaoDao.buscarPorId(id)
                        } catch (e: Exception) {
                            Log.e(
                                TAG_BANCO,
                                "Erro ao buscar cartão por id",
                                e
                            )
                            null
                        }
                    }
                }

                TelaDetalheCartao(
                    cartao = cartaoDetalhe,
                    onBack = {
                        navController.popBackStack()
                    },
                    onAtualizar = { atualizado ->
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                cartaoDao.atualizar(atualizado)

                                val recarregado =
                                    cartaoDao.buscarPorId(id)

                                val listaAtualizada =
                                    cartaoDao.buscarTodos()

                                withContext(Dispatchers.Main) {
                                    cartaoDetalhe = recarregado
                                    cartoes = listaAtualizada
                                }
                            } catch (e: Exception) {
                                Log.e(
                                    TAG_BANCO,
                                    "Erro ao atualizar cartão",
                                    e
                                )
                            }
                        }
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
                    navController.navigate(item.rota) {
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
                        contentDescription = item.titulo
                    )
                },
                label = {
                    Text(item.titulo)
                }
            )
        }
    }
}
