package com.ufps.impostor.domain.model

// Resultado de repartir una partida nueva
data class GameSetup(
    val players: List<Player>,
    val secretWord: GameWord,
    val firstCluePlayer: String
)
