package com.example.myapplication.ui.home

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.navigation.Rotas
import com.example.myapplication.ui.trazerdinheiro.PopUpTrazerDinheiro

private val PinkPrimary = Color(0xFFE6007E)
private val PinkLight = Color(0xFFF7DCE1)
private val BackgroundGray = Color(0xFFF2F2F2)

@Composable
fun TelaHome(
    navController: NavHostController
) {
    var saldo by remember {
        mutableDoubleStateOf(67.67)
    }

    var mostrarPopUpTrazerDinheiro by remember {
        mutableStateOf(false)
    }

    if (mostrarPopUpTrazerDinheiro) {
        PopUpTrazerDinheiro(
            onDismiss = {
                mostrarPopUpTrazerDinheiro = false
            },
            onConfirmar = { valorAdicionado ->
                saldo += valorAdicionado
            }
        )
    }

    Scaffold(
        containerColor = BackgroundGray,
        topBar = {
            CabecalhoRosa()
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Spacer(
                modifier = Modifier.height(32.dp)
            )

            SaldoConta(
                saldo = saldo
            )

            Spacer(
                modifier = Modifier.height(34.dp)
            )

            AcoesRapidas(
                onPix = {
                    navController.navigate(Rotas.PIX)
                },
                onTrazerDinheiro = {
                    mostrarPopUpTrazerDinheiro = true
                },
                onPagar = {
                    navController.navigate(Rotas.FATURA)
                },
                onRecarga = {
                    navController.navigate(Rotas.PIX)
                }
            )

            Spacer(
                modifier = Modifier.height(34.dp)
            )

            BannerInvestimentos()

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            CartaoMeusCartoes(
                onClick = {
                    navController.navigate(Rotas.CARTOES)
                }
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            CartaoEmprestimo(
                onClick = {
                    navController.navigate(Rotas.CARTOES)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CabecalhoRosa() {
    TopAppBar(
        title = {
            Text(
                text = "Lucas",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Perfil",
                tint = Color.White,
                modifier = Modifier.padding(
                    start = 12.dp
                )
            )
        },
        actions = {
            Icon(
                Icons.Default.Visibility,
                "Visibilidade",
                tint = Color.White
            )

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            Icon(
                Icons.Default.Build,
                "Configurações",
                tint = Color.White
            )

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            Icon(
                Icons.Default.Menu,
                "Menu",
                tint = Color.White
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PinkPrimary
        )
    )
}

@Composable
private fun SaldoConta(
    saldo: Double
) {
    val saldoFormatado = "R$ %.2f".format(saldo).replace('.', ',')

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Saldo em conta",
                fontSize = 22.sp,
                color = Color.Gray
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = saldoFormatado,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.DarkGray
        )
    }
}

@Composable
private fun AcoesRapidas(
    onPix: () -> Unit,
    onTrazerDinheiro: () -> Unit,
    onPagar: () -> Unit,
    onRecarga: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp
            ),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        AcaoCircular(
            Icons.Default.Diamond,
            "PIX",
            onPix
        )

        AcaoCircular(
            Icons.Default.ArrowDownward,
            "Trazer dinheiro",
            onTrazerDinheiro
        )

        AcaoCircular(
            Icons.Default.ShoppingBag,
            "Pagar",
            onPagar
        )

        AcaoCircular(
            Icons.Default.Call,
            "Recarga",
            onRecarga
        )
    }
}

@Composable
private fun AcaoCircular(
    icon: ImageVector,
    texto: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(72.dp)
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.size(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = PinkPrimary
            ),
            contentPadding = PaddingValues(0.dp),
            elevation = null
        ) {
            Icon(
                icon,
                texto,
                tint = Color.White
            )
        }

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = texto,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            color = Color.DarkGray
        )
    }
}

@Composable
private fun BannerInvestimentos() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp
            ),
        shape = RoundedCornerShape(16.dp),
        color = PinkPrimary
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 28.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.TrendingUp,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )

            Spacer(
                modifier = Modifier.width(22.dp)
            )

            Column {
                Text(
                    text = "Investimentos",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )

                Text(
                    text = "Confira as melhores ações do momento!",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun CartaoMeusCartoes(
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp
            )
            .clickable(
                onClick = onClick
            ),
        shape = RoundedCornerShape(20.dp),
        color = PinkLight
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Meus cartões",
                fontWeight = FontWeight.Medium
            )

            Icon(
                Icons.Default.CreditCard,
                contentDescription = null,
                tint = Color.DarkGray
            )
        }
    }
}

@Composable
private fun CartaoEmprestimo(
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp
            )
            .clickable(
                onClick = onClick
            ),
        shape = RoundedCornerShape(20.dp),
        color = PinkLight
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Empréstimo",
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Valor disponível de até: R$50.000",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.DarkGray
            )
        }
    }
}
