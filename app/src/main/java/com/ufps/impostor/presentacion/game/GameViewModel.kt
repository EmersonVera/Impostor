package com.ufps.impostor.presentacion.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ufps.impostor.data.repository.WordRepositoryImpl
import com.ufps.impostor.domain.model.EliminationRecord
import com.ufps.impostor.domain.model.VoteResult
import com.ufps.impostor.domain.use_case.AddPlayerUseCase
import com.ufps.impostor.domain.use_case.GetMaxImpostorsUseCase
import com.ufps.impostor.domain.use_case.GetWordsUseCase
import com.ufps.impostor.domain.use_case.StartGameUseCase
import com.ufps.impostor.domain.use_case.StartNextRoundUseCase
import com.ufps.impostor.domain.use_case.SubmitVoteUseCase
import kotlinx.coroutines.launch

// Todas las pantallas comparten una misma partida, por eso hay un solo ViewModel
class GameViewModel(
    private val getWordsUseCase: GetWordsUseCase,
    private val addPlayerUseCase: AddPlayerUseCase,
    private val getMaxImpostorsUseCase: GetMaxImpostorsUseCase,
    private val startGameUseCase: StartGameUseCase,
    private val submitVoteUseCase: SubmitVoteUseCase,
    private val startNextRoundUseCase: StartNextRoundUseCase
) : ViewModel() {

    var state by mutableStateOf(GameState())
        private set

    init {
        loadWords()
    }

    private fun loadWords() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, errorMessage = null)
            getWordsUseCase.invocar().onSuccess {
                state = state.copy(isLoading = false, words = it)
            }.onFailure {
                state = state.copy(isLoading = false, errorMessage = it.message)
            }
        }
    }

    fun onNameInputChange(value: String) {
        state = state.copy(nameInput = value.take(MAX_NAME_LENGTH), nameError = null)
    }

    fun addPlayer() {
        if (state.nameInput.isBlank()) return
        addPlayerUseCase.invocar(state.playerNames, state.nameInput).onSuccess {
            val names = state.playerNames + it
            state = state.copy(
                playerNames = names,
                nameInput = "",
                nameError = null,
                maxImpostors = getMaxImpostorsUseCase.invocar(names.size)
            )
        }.onFailure {
            state = state.copy(nameError = it.message)
        }
    }

    fun removePlayer(name: String) {
        val names = state.playerNames - name
        val max = getMaxImpostorsUseCase.invocar(names.size)
        state = state.copy(
            playerNames = names,
            maxImpostors = max,
            impostorCount = minOf(state.impostorCount, max)
        )
    }

    fun increaseImpostors() {
        if (state.impostorCount < state.maxImpostors) {
            state = state.copy(impostorCount = state.impostorCount + 1)
        }
    }

    fun decreaseImpostors() {
        if (state.impostorCount > 1) {
            state = state.copy(impostorCount = state.impostorCount - 1)
        }
    }

    // Devuelve true si la partida se pudo repartir
    fun startGame(): Boolean {
        var started = false
        startGameUseCase.invocar(state.playerNames, state.impostorCount, state.words).onSuccess {
            started = true
            state = state.copy(
                errorMessage = null,
                players = it.players,
                secretWord = it.secretWord,
                firstCluePlayer = it.firstCluePlayer,
                currentTurnIndex = 0,
                isHolding = false,
                hasRevealed = false,
                currentRound = 1,
                selectedSuspect = null,
                lastEliminated = null,
                roundHistory = emptyList()
            )
        }.onFailure {
            state = state.copy(errorMessage = it.message)
        }
        return started
    }

    fun onRevealPressed() {
        state = state.copy(isHolding = true, hasRevealed = true)
    }

    fun onRevealReleased() {
        state = state.copy(isHolding = false)
    }

    // Devuelve true cuando ya todos vieron su rol y sigue la ronda de pistas
    fun nextTurn(): Boolean {
        if (state.currentTurnIndex >= state.players.size - 1) return true
        state = state.copy(
            currentTurnIndex = state.currentTurnIndex + 1,
            isHolding = false,
            hasRevealed = false
        )
        return false
    }

    fun selectSuspect(name: String) {
        state = state.copy(selectedSuspect = name)
    }

    fun confirmVote(): VoteResult? {
        val suspect = state.selectedSuspect ?: return null
        val outcome = submitVoteUseCase.invocar(state.players, suspect).getOrNull() ?: return null
        val record = EliminationRecord(
            round = state.currentRound,
            playerName = outcome.eliminated.name,
            wasImpostor = outcome.eliminated.isImpostor
        )
        state = state.copy(
            players = outcome.players,
            lastEliminated = outcome.eliminated,
            roundHistory = state.roundHistory + record,
            selectedSuspect = null
        )
        return outcome.result
    }

    fun startNextRound() {
        state = state.copy(
            currentRound = state.currentRound + 1,
            firstCluePlayer = startNextRoundUseCase.invocar(state.players) ?: state.firstCluePlayer,
            selectedSuspect = null
        )
    }

    companion object {
        private const val MAX_NAME_LENGTH = 15

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                GameViewModel(
                    GetWordsUseCase(WordRepositoryImpl()),
                    AddPlayerUseCase(),
                    GetMaxImpostorsUseCase(),
                    StartGameUseCase(),
                    SubmitVoteUseCase(),
                    StartNextRoundUseCase()
                )
            }
        }
    }
}
