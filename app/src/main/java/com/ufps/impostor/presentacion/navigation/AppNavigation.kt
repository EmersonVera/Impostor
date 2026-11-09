package com.ufps.impostor.presentacion.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ufps.impostor.domain.model.VoteResult
import com.ufps.impostor.presentacion.config.ScreenConfig
import com.ufps.impostor.presentacion.entrega.ScreenEntrega
import com.ufps.impostor.presentacion.game.GameViewModel
import com.ufps.impostor.presentacion.home.Screen
import com.ufps.impostor.presentacion.home.ScreenHome
import com.ufps.impostor.presentacion.pistas.ScreenPistas
import com.ufps.impostor.presentacion.reglas.ScreenReglas
import com.ufps.impostor.presentacion.resultado.ScreenCivilesGanan
import com.ufps.impostor.presentacion.resultado.ScreenEliminado
import com.ufps.impostor.presentacion.resultado.ScreenImpostorGana
import com.ufps.impostor.presentacion.votacion.ScreenVotacion
import com.ufps.impostor.ui.theme.BgAccentLight
import com.ufps.impostor.ui.theme.BgMain
import com.ufps.impostor.ui.theme.PrimaryRed
import com.ufps.impostor.ui.theme.TextMuted

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: GameViewModel = viewModel(factory = GameViewModel.Factory)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Deja Home como única pantalla debajo del destino, así atrás siempre vuelve al inicio
    val goTo: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(Screen.home.route)
            launchSingleTop = true
        }
    }
    val goHome: () -> Unit = {
        navController.navigate(Screen.home.route) {
            popUpTo(Screen.home.route) { inclusive = true }
        }
    }
    val startGame: () -> Unit = {
        if (viewModel.startGame()) goTo(Screen.entrega.route)
    }

    // Si Android recreó la app, el ViewModel es nuevo y ya no existe la partida en curso
    val gameRoutes = listOf(
        Screen.entrega.route,
        Screen.pistas.route,
        Screen.votacion.route,
        Screen.eliminado.route,
        Screen.civilesGanan.route,
        Screen.impostorGana.route
    )
    val hasNoGame = viewModel.state.players.isEmpty()
    LaunchedEffect(currentRoute, hasNoGame) {
        if (currentRoute in gameRoutes && hasNoGame) goHome()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BgMain,
        bottomBar = {
            val itemColors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryRed,
                selectedTextColor = PrimaryRed,
                indicatorColor = BgAccentLight,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            )
            NavigationBar(containerColor = BgMain) {
                NavigationBarItem(
                    selected = currentRoute != Screen.reglas.route,
                    onClick = goHome,
                    icon = { Icon(Icons.Filled.Person, contentDescription = null) },
                    label = { Text("Lobby", style = MaterialTheme.typography.labelMedium) },
                    colors = itemColors
                )
                NavigationBarItem(
                    selected = currentRoute == Screen.reglas.route,
                    onClick = { goTo(Screen.reglas.route) },
                    icon = { Icon(Icons.Filled.Info, contentDescription = null) },
                    label = { Text("Rules", style = MaterialTheme.typography.labelMedium) },
                    colors = itemColors
                )
            }
        }
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.home.route,
            modifier = Modifier.padding(contentPadding)
        ) {
            composable(Screen.home.route) {
                ScreenHome(
                    onPlay = { goTo(Screen.config.route) },
                    onRules = { goTo(Screen.reglas.route) }
                )
            }
            composable(Screen.reglas.route) {
                ScreenReglas(
                    onBack = goHome,
                    onPlay = { goTo(Screen.config.route) }
                )
            }
            composable(Screen.config.route) {
                ScreenConfig(
                    viewModel = viewModel,
                    onBack = goHome,
                    onStart = startGame
                )
            }
            composable(Screen.entrega.route) {
                ScreenEntrega(
                    viewModel = viewModel,
                    onBack = { goTo(Screen.config.route) },
                    onAllRevealed = { goTo(Screen.pistas.route) }
                )
            }
            composable(Screen.pistas.route) {
                ScreenPistas(
                    viewModel = viewModel,
                    onGoToVote = { navController.navigate(Screen.votacion.route) }
                )
            }
            composable(Screen.votacion.route) {
                ScreenVotacion(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onVoted = { result ->
                        when (result) {
                            VoteResult.gameContinues -> goTo(Screen.eliminado.route)
                            VoteResult.civiliansWin -> goTo(Screen.civilesGanan.route)
                            VoteResult.impostorsWin -> goTo(Screen.impostorGana.route)
                        }
                    }
                )
            }
            composable(Screen.eliminado.route) {
                ScreenEliminado(
                    viewModel = viewModel,
                    onBack = goHome,
                    onNextRound = {
                        viewModel.startNextRound()
                        goTo(Screen.pistas.route)
                    }
                )
            }
            composable(Screen.civilesGanan.route) {
                ScreenCivilesGanan(
                    viewModel = viewModel,
                    onBack = goHome,
                    onPlayAgain = startGame
                )
            }
            composable(Screen.impostorGana.route) {
                ScreenImpostorGana(
                    viewModel = viewModel,
                    onBack = goHome,
                    onPlayAgain = startGame
                )
            }
        }
    }
}
