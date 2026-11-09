package com.ufps.impostor.presentacion.game

import com.ufps.impostor.domain.model.EliminationRecord
import com.ufps.impostor.domain.model.GameWord
import com.ufps.impostor.domain.model.Player

data class GameState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val words: List<GameWord> = emptyList(),

    // Configuración de la partida
    val playerNames: List<String> = emptyList(),
    val nameInput: String = "",
    val nameError: String? = null,
    val impostorCount: Int = 1,
    val maxImpostors: Int = 1,

    // Entrega secreta
    val players: List<Player> = emptyList(),
    val secretWord: GameWord? = null,
    val currentTurnIndex: Int = 0,
    val isHolding: Boolean = false,
    val hasRevealed: Boolean = false,

    // Rondas y votación
    val currentRound: Int = 1,
    val firstCluePlayer: String = "",
    val selectedSuspect: String? = null,
    val lastEliminated: Player? = null,
    val roundHistory: List<EliminationRecord> = emptyList()
)
