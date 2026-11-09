package com.ufps.impostor.presentacion.home

sealed class Screen(val route: String) {
    object home : Screen("home")
    object config : Screen("config")
    object entrega : Screen("entrega")
    object pistas : Screen("pistas")
    object votacion : Screen("votacion")
    object eliminado : Screen("eliminado")
    object civilesGanan : Screen("civiles_ganan")
    object impostorGana : Screen("impostor_gana")
    object reglas : Screen("reglas")
}
