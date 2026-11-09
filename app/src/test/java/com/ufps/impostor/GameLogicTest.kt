package com.ufps.impostor

import com.ufps.impostor.domain.model.GameWord
import com.ufps.impostor.domain.model.Player
import com.ufps.impostor.domain.model.VoteResult
import com.ufps.impostor.domain.use_case.AddPlayerUseCase
import com.ufps.impostor.domain.use_case.GetMaxImpostorsUseCase
import com.ufps.impostor.domain.use_case.StartGameUseCase
import com.ufps.impostor.domain.use_case.StartNextRoundUseCase
import com.ufps.impostor.domain.use_case.SubmitVoteUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class GameLogicTest {

    private val words = listOf(GameWord("PIZZA", listOf("masa", "queso", "tomate"), "Comida"))
    private val names = listOf("Ana", "Beto", "Caro", "Dani", "Eva")

    private fun player(id: Int, name: String, impostor: Boolean, alive: Boolean = true) =
        Player(id, name, name.take(1), isImpostor = impostor, isAlive = alive)

    @Test
    fun addPlayer_limpiaEspacios() {
        assertEquals("Ana", AddPlayerUseCase().invocar(emptyList(), "  Ana ").getOrNull())
    }

    @Test
    fun addPlayer_rechazaDuplicadosSinImportarMayusculas() {
        val result = AddPlayerUseCase().invocar(listOf("Ana"), "ANA")
        assertEquals("Ese nombre ya está en la lista", result.exceptionOrNull()?.message)
    }

    @Test
    fun addPlayer_rechazaVacio() {
        assertTrue(AddPlayerUseCase().invocar(emptyList(), "   ").isFailure)
    }

    @Test
    fun maxImpostores_sigueLaFormulaDeLaWeb() {
        val useCase = GetMaxImpostorsUseCase()
        val expected = mapOf(0 to 1, 3 to 1, 4 to 1, 5 to 2, 6 to 2, 7 to 3, 8 to 3)
        expected.forEach { (players, max) -> assertEquals(max, useCase.invocar(players)) }
    }

    @Test
    fun startGame_exigeTresJugadores() {
        assertTrue(StartGameUseCase().invocar(listOf("Ana", "Beto"), 1, words).isFailure)
    }

    @Test
    fun startGame_fallaSinPalabras() {
        assertTrue(StartGameUseCase().invocar(names, 1, emptyList()).isFailure)
    }

    @Test
    fun startGame_repartePapelesCorrectamente() {
        repeat(200) { seed ->
            val setup = StartGameUseCase(Random(seed)).invocar(names, 2, words).getOrThrow()
            val impostors = setup.players.filter { it.isImpostor }

            assertEquals(names, setup.players.map { it.name })
            assertEquals(2, impostors.size)
            assertTrue(setup.players.all { it.isAlive })
            assertTrue(setup.firstCluePlayer in names)
            assertTrue(impostors.all { it.hint in words[0].hints })
            assertEquals(2, impostors.map { it.hint }.distinct().size)
            assertTrue(setup.players.filter { !it.isImpostor }.all { it.hint == null })
        }
    }

    @Test
    fun startGame_reciclaPistasSiHayMasImpostoresQueFragmentos() {
        val oneHint = listOf(GameWord("PIZZA", listOf("masa"), "Comida"))
        val setup = StartGameUseCase(Random(1)).invocar(names, 2, oneHint).getOrThrow()
        assertTrue(setup.players.filter { it.isImpostor }.all { it.hint == "masa" })
    }

    @Test
    fun voto_alUnicoImpostorDaVictoriaACiviles() {
        val players = listOf(player(1, "A", true), player(2, "B", false), player(3, "C", false))
        val outcome = SubmitVoteUseCase().invocar(players, "A").getOrThrow()
        assertEquals(VoteResult.civiliansWin, outcome.result)
        assertTrue(!outcome.eliminated.isAlive)
    }

    @Test
    fun voto_aCivilConTresJugadoresDaVictoriaAlImpostor() {
        val players = listOf(player(1, "A", true), player(2, "B", false), player(3, "C", false))
        assertEquals(
            VoteResult.impostorsWin,
            SubmitVoteUseCase().invocar(players, "B").getOrThrow().result
        )
    }

    @Test
    fun voto_aCivilContinuaSiQuedanMasCiviles() {
        val players = listOf(
            player(1, "A", true),
            player(2, "B", false),
            player(3, "C", false),
            player(4, "D", false),
            player(5, "E", false)
        )
        assertEquals(
            VoteResult.gameContinues,
            SubmitVoteUseCase().invocar(players, "B").getOrThrow().result
        )
    }

    @Test
    fun voto_aUnImpostorContinuaSiQuedanOtros() {
        val players = listOf(
            player(1, "A", true),
            player(2, "B", true),
            player(3, "C", false),
            player(4, "D", false),
            player(5, "E", false)
        )
        val outcome = SubmitVoteUseCase().invocar(players, "A").getOrThrow()
        assertEquals(VoteResult.gameContinues, outcome.result)
        assertEquals(4, outcome.players.count { it.isAlive })
    }

    @Test
    fun voto_ultimoImpostorConVariosGanaCivilesAunqueHayaEliminados() {
        val players = listOf(
            player(1, "A", true, alive = false),
            player(2, "B", true),
            player(3, "C", false),
            player(4, "D", false),
            player(5, "E", false)
        )
        assertEquals(
            VoteResult.civiliansWin,
            SubmitVoteUseCase().invocar(players, "B").getOrThrow().result
        )
    }

    @Test
    fun voto_aJugadorEliminadoEsInvalido() {
        val players = listOf(player(1, "A", true, alive = false), player(2, "B", false))
        assertTrue(SubmitVoteUseCase().invocar(players, "A").isFailure)
    }

    @Test
    fun siguienteRonda_soloEligeJugadoresVivos() {
        val players = listOf(
            player(1, "A", true, alive = false),
            player(2, "B", false),
            player(3, "C", false, alive = false),
            player(4, "D", false)
        )
        repeat(100) { seed ->
            val starter = StartNextRoundUseCase(Random(seed)).invocar(players)
            assertNotNull(starter)
            assertTrue(starter in listOf("B", "D"))
        }
    }

    @Test
    fun siguienteRonda_sinVivosDevuelveNull() {
        val players = listOf(player(1, "A", true, alive = false))
        assertNull(StartNextRoundUseCase().invocar(players))
    }
}
