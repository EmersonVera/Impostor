package com.ufps.impostor.presentacion.pistas

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.unit.dp
import com.ufps.impostor.presentacion.components.Badge
import com.ufps.impostor.presentacion.components.Header
import com.ufps.impostor.presentacion.components.NeoButton
import com.ufps.impostor.presentacion.components.NeoCard
import com.ufps.impostor.presentacion.components.PlayerAvatar
import com.ufps.impostor.presentacion.game.GameViewModel
import com.ufps.impostor.ui.theme.BgAccentLight
import com.ufps.impostor.ui.theme.BorderLight
import com.ufps.impostor.ui.theme.NeutralBadge
import com.ufps.impostor.ui.theme.PrimaryRed
import com.ufps.impostor.ui.theme.TextMuted

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ScreenPistas(viewModel: GameViewModel, onGoToVote: () -> Unit) {
    val state = viewModel.state
    val alivePlayers = state.players.filter { it.isAlive }

    // En la web esta pantalla no tiene botón de volver: se sale por la barra inferior
    BackHandler {}

    Column(modifier = Modifier.fillMaxSize()) {
        Header(subtitle = "Ronda ${state.currentRound} • Pistas & Debate")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "FASE DE PISTAS • RONDA ${state.currentRound}",
                style = MaterialTheme.typography.labelLarge,
                color = PrimaryRed
            )
            Text(text = "Ronda de Pistas", style = MaterialTheme.typography.headlineMedium)
            Text(
                text = "Cada jugador en la mesa dirá una palabra o pista corta en voz alta.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )

            NeoCard {
                Text(text = "INICIA LA RONDA", style = MaterialTheme.typography.labelLarge, color = TextMuted)
                Text(
                    text = state.firstCluePlayer,
                    style = MaterialTheme.typography.headlineMedium,
                    color = PrimaryRed
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Sigan el turno en el sentido de las agujas del reloj alrededor de la mesa.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }

            NeoCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "JUGADORES EN LA MESA", style = MaterialTheme.typography.titleSmall)
                    Badge("${alivePlayers.size} activos", background = NeutralBadge, color = TextMuted)
                }
                Spacer(Modifier.height(10.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    alivePlayers.forEach { player ->
                        Row(
                            modifier = Modifier
                                .border(BorderStroke(1.dp, BorderLight), RoundedCornerShape(50))
                                .padding(start = 4.dp, end = 10.dp, top = 4.dp, bottom = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PlayerAvatar(player.letter, player.id - 1, size = 26.dp)
                            Spacer(Modifier.width(6.dp))
                            Text(text = player.name, style = MaterialTheme.typography.labelLarge)
                            if (player.name == state.firstCluePlayer) {
                                Spacer(Modifier.width(6.dp))
                                Badge("1°")
                            }
                        }
                    }
                }
            }

            NeoCard(background = BgAccentLight, borderColor = PrimaryRed) {
                Text(text = "CONSEJO PRO", style = MaterialTheme.typography.labelLarge, color = PrimaryRed)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "No seas demasiado específico para no revelar el secreto al impostor, ni demasiado vago para no parecer culpable.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            NeoButton(text = "Ir a Votación", onClick = onGoToVote)
        }
    }
}
