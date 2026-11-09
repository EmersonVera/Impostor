package com.ufps.impostor.domain.use_case

import com.ufps.impostor.domain.model.Player
import com.ufps.impostor.domain.model.VoteOutcome
import com.ufps.impostor.domain.model.VoteResult

class SubmitVoteUseCase {

    fun invocar(players: List<Player>, votedName: String): Result<VoteOutcome> {
        val voted = players.find { it.name == votedName && it.isAlive }
            ?: return Result.failure(Exception("Jugador no encontrado"))

        val updatedPlayers = players.map {
            if (it.name == votedName) it.copy(isAlive = false) else it
        }
        val remainingCivilians = updatedPlayers.count { !it.isImpostor && it.isAlive }
        val remainingImpostors = updatedPlayers.count { it.isImpostor && it.isAlive }

        val result = when {
            voted.isImpostor && remainingImpostors == 0 -> VoteResult.civiliansWin
            voted.isImpostor -> VoteResult.gameContinues
            remainingImpostors >= remainingCivilians -> VoteResult.impostorsWin
            else -> VoteResult.gameContinues
        }
        return Result.success(VoteOutcome(updatedPlayers, voted.copy(isAlive = false), result))
    }
}
