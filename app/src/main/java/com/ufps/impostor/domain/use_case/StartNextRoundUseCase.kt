package com.ufps.impostor.domain.use_case

import com.ufps.impostor.domain.model.Player
import kotlin.random.Random

class StartNextRoundUseCase(private val random: Random = Random.Default) {
    // Devuelve el jugador que abre la ronda, elegido solo entre los que siguen vivos
    fun invocar(players: List<Player>): String? {
        return players.filter { it.isAlive }.randomOrNull(random)?.name
    }
}
