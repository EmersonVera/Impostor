package com.ufps.impostor

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameFlowTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private val names = listOf("Ana", "Beto", "Caro")

    private fun text(value: String) = rule.onNodeWithText(value)

    private fun isVisible(value: String): Boolean {
        return rule.onAllNodes(hasText(value)).fetchSemanticsNodes().isNotEmpty()
    }

    private fun addPlayers() {
        names.forEach { name ->
            rule.onNode(hasSetTextAction()).performTextInput(name)
            text("Añadir").performClick()
        }
    }

    // Recorre la entrega secreta y devuelve los impostores que vio en pantalla
    private fun revealAllRoles(): List<String> {
        val impostors = mutableListOf<String>()
        names.forEachIndexed { index, name ->
            text("JUGADOR ${index + 1} DE 3").assertExists()
            text(name).assertExists()
            text("CONTENIDO CONFIDENCIAL").assertExists()

            text("Mantén presionado para ver").performScrollTo().performTouchInput { down(center) }
            rule.waitForIdle()
            text("SUELTA PARA OCULTAR").assertExists()
            if (isVisible("ERES EL IMPOSTOR")) {
                impostors.add(name)
                text("PISTA DE CAMUFLAJE").assertExists()
            } else {
                text("ERES CIVIL").assertExists()
                text("PALABRA CLAVE").assertExists()
            }
            text("SUELTA PARA OCULTAR").performTouchInput { up() }
            rule.waitForIdle()

            // El secreto se oculta al soltar
            text("CONTENIDO CONFIDENCIAL").assertExists()
            val next = if (index == 2) "Comenzar ronda de pistas" else "Ya la vi, pasar al siguiente"
            text(next).performScrollTo().performClick()
            rule.waitForIdle()
        }
        return impostors
    }

    private fun voteFor(name: String) {
        text("Ir a Votación").performScrollTo().performClick()
        rule.waitForIdle()
        text("Confirmar voto").assertIsNotEnabled()
        text(name).performClick()
        text("SOSPECHOSO").assertExists()
        text("Confirmar voto").assertIsEnabled().performClick()
        rule.waitForIdle()
    }

    @Test
    fun configuracion_validaJugadores() {
        text("Jugar").performClick()
        rule.waitForIdle()
        text("Comenzar partida").performScrollTo().assertIsNotEnabled()

        rule.onNode(hasSetTextAction()).performTextInput("Ana")
        text("Añadir").performClick()
        rule.waitForIdle()
        rule.onNode(hasSetTextAction()).performTextInput("ana")
        text("Añadir").performClick()
        rule.waitForIdle()
        text("Ese nombre ya está en la lista").assertExists()
    }

    @Test
    fun barraInferior_navegaEntreLobbyYReglas() {
        text("Rules").performClick()
        rule.waitForIdle()
        text("Reglas del juego").assertExists()
        text("Lobby").performClick()
        rule.waitForIdle()
        text("Juego social de deducción cara a cara").assertExists()
    }

    @Test
    fun partidaCompleta_revanchaYVictoriaDeCiviles() {
        text("Jugar").performClick()
        rule.waitForIdle()
        addPlayers()
        rule.waitForIdle()
        text("Comenzar partida").performScrollTo().assertIsEnabled().performClick()
        rule.waitForIdle()

        // Primera partida: se vota a un civil y con 3 jugadores gana el impostor
        val firstImpostors = revealAllRoles()
        assertEquals(1, firstImpostors.size)
        text("Ronda de Pistas").assertExists()
        voteFor(names.first { it !in firstImpostors })
        text("¡EL IMPOSTOR\nHA GANADO!").assertExists()

        // Revancha: se vota al impostor y ganan los civiles
        text("JUGAR REVANCHA").performScrollTo().performClick()
        rule.waitForIdle()
        val secondImpostors = revealAllRoles()
        assertEquals(1, secondImpostors.size)
        voteFor(secondImpostors.first())
        text("¡Los Civiles Ganan!").assertExists()

        text("VOLVER AL MENÚ PRINCIPAL").performScrollTo().performClick()
        rule.waitForIdle()
        text("Juego social de deducción cara a cara").assertExists()
    }
}
