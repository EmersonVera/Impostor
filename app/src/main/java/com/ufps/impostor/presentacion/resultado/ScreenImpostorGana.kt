package com.ufps.impostor.presentacion.resultado

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ufps.impostor.domain.model.Player
import com.ufps.impostor.presentacion.components.Badge
import com.ufps.impostor.presentacion.components.Header
import com.ufps.impostor.presentacion.components.NeoButton
import com.ufps.impostor.presentacion.components.NeoCard
import com.ufps.impostor.presentacion.components.PlayerAvatar
import com.ufps.impostor.presentacion.components.neoShadow
import com.ufps.impostor.presentacion.game.GameViewModel
import com.ufps.impostor.ui.theme.BorderDark
import com.ufps.impostor.ui.theme.BorderLight
import com.ufps.impostor.ui.theme.NeutralBadge
import com.ufps.impostor.ui.theme.PrimaryRed
import com.ufps.impostor.ui.theme.TextMuted
import com.ufps.impostor.ui.theme.WinnerBadge
import com.ufps.impostor.ui.theme.WinnerEnd
import com.ufps.impostor.ui.theme.WinnerStart

@Composable
fun ScreenImpostorGana(viewModel: GameViewModel, onBack: () -> Unit, onPlayAgain: () -> Unit) {
    val state = viewModel.state
    val secretWord = state.secretWord ?: return
    val allImpostors = state.players.filter { it.isImpostor }
    val winners = allImpostors.filter { it.isAlive }
    val civilians = state.players.count { !it.isImpostor && it.isAlive }
    val isSingleWinner = winners.size == 1

    Column(modifier = Modifier.fillMaxSize()) {
        Header(subtitle = "Round Result", onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isSingleWinner) "¡EL IMPOSTOR\nHA GANADO!" else "¡LOS IMPOSTORES\nHAN GANADO!",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Los impostores igualaron en número a los civiles (${winners.size} Impostores = $civilians Civiles). El engaño fue total y se apoderaron del control definitivo de la sala.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
                textAlign = TextAlign.Center
            )

            WinnerCard(winners)
            Roster(allImpostors, viewModel)

            NeoCard {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "PALABRA OCULTA JAMÁS REVELADA",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextMuted
                    )
                    Text(
                        text = secretWord.word,
                        style = MaterialTheme.typography.headlineMedium,
                        color = PrimaryRed,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Tema: ${secretWord.topic}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
            }

            NeoButton(text = "JUGAR REVANCHA", onClick = onPlayAgain)
            NeoButton(text = "VOLVER AL MENÚ PRINCIPAL", onClick = onBack, primary = false)
        }
    }
}

@Composable
private fun WinnerCard(winners: List<Player>) {
    val shape = RoundedCornerShape(20.dp)
    val isSingleWinner = winners.size == 1
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .neoShadow(4.dp, 20.dp)
            .clip(shape)
            .background(Brush.linearGradient(listOf(WinnerStart, WinnerEnd)))
            .border(BorderStroke(2.5.dp, BorderDark), shape)
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isSingleWinner) "GANADOR DE LA PARTIDA" else "GANADORES DE LA PARTIDA",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f)
            )
            Badge("SOBREVIVIÓ", background = WinnerEnd, color = WinnerBadge)
        }
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            winners.forEach { winner ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "👑", style = MaterialTheme.typography.titleMedium)
                    PlayerAvatar(winner.letter, winner.id - 1, size = 48.dp)
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = if (isSingleWinner) "Infiltrado que quedó vivo:" else "Infiltrados que quedaron vivos:",
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted
        )
        Text(
            text = winners.joinToString(" y ") { it.name },
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = if (isSingleWinner) {
                "Engañó a toda la mesa, esquivó las sospechas y se llevó la victoria."
            } else {
                "Lograron mantenerse con vida y sellar el triunfo del equipo infiltrado."
            },
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted
        )
    }
}

@Composable
private fun Roster(impostors: List<Player>, viewModel: GameViewModel) {
    NeoCard {
        Text(text = "¿Quiénes eran los impostores?", style = MaterialTheme.typography.titleSmall)
        Text(
            text = "${impostors.size} ${if (impostors.size == 1) "infiltrado" else "infiltrados"} en total",
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted
        )
        Spacer(Modifier.height(8.dp))
        impostors.forEach { impostor ->
            val round = viewModel.state.roundHistory.find { it.playerName == impostor.name }?.round
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(BorderStroke(1.dp, BorderLight), RoundedCornerShape(14.dp))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlayerAvatar(impostor.letter, impostor.id - 1)
                Spacer(Modifier.width(10.dp))
                Text(
                    text = impostor.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                if (impostor.isAlive) {
                    Badge("¡GANÓ! (VIVO)", background = WinnerEnd, color = WinnerBadge)
                } else {
                    Badge(
                        text = if (round != null) "Eliminado en R$round" else "Eliminado",
                        background = NeutralBadge,
                        color = TextMuted
                    )
                }
            }
        }
    }
}
