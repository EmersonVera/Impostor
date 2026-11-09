package com.ufps.impostor.domain.use_case

import com.ufps.impostor.domain.model.GameSetup
import com.ufps.impostor.domain.model.GameWord
import com.ufps.impostor.domain.model.Player
import kotlin.random.Random

class StartGameUseCase(private val random: Random = Random.Default) {

    fun invocar(
        playerNames: List<String>,
        impostorCount: Int,
        words: List<GameWord>
    ): Result<GameSetup> {
        return when {
            playerNames.size < MIN_PLAYERS -> {
                Result.failure(Exception("Añade al menos 3 jugadores para poder iniciar la partida."))
            }
            words.isEmpty() -> {
                Result.failure(Exception("No hay palabras disponibles"))
            }
            else -> Result.success(deal(playerNames, impostorCount, words))
        }
    }

    private fun deal(
        playerNames: List<String>,
        impostorCount: Int,
        words: List<GameWord>
    ): GameSetup {
        val secretWord = words.random(random)
        val hints = secretWord.hints.shuffled(random)
        val impostorNames = playerNames.shuffled(random).take(impostorCount)

        // 1. Cada impostor recibe una pista distinta mientras alcancen los fragmentos
        var impostorCounter = 0
        val players = playerNames.mapIndexed { index, name ->
            val isImpostor = name in impostorNames
            var hint: String? = null
            if (isImpostor) {
                hint = hints[impostorCounter % hints.size]
                impostorCounter++
            }
            Player(
                id = index + 1,
                name = name,
                letter = name.first().uppercase(),
                isImpostor = isImpostor,
                hint = hint
            )
        }

        // 2. El jugador que abre la ronda de pistas se elige al azar
        return GameSetup(players, secretWord, playerNames.random(random))
    }

    companion object {
        const val MIN_PLAYERS = 3
    }
}
