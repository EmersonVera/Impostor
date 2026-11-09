package com.ufps.impostor.domain.model

// Qué ocurre con la partida después de una votación
sealed class VoteResult {
    object gameContinues : VoteResult()
    object civiliansWin : VoteResult()
    object impostorsWin : VoteResult()
}

data class VoteOutcome(
    val players: List<Player>,
    val eliminated: Player,
    val result: VoteResult
)
