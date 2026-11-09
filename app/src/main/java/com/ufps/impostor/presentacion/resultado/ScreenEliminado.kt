package com.ufps.impostor.presentacion.resultado

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ufps.impostor.presentacion.components.Badge
import com.ufps.impostor.presentacion.components.Header
import com.ufps.impostor.presentacion.components.NeoButton
import com.ufps.impostor.presentacion.components.NeoCard
import com.ufps.impostor.presentacion.game.GameViewModel
import com.ufps.impostor.ui.theme.BgCardSubtle
import com.ufps.impostor.ui.theme.BorderDark
import com.ufps.impostor.ui.theme.DangerBackground
import com.ufps.impostor.ui.theme.DangerBorder
import com.ufps.impostor.ui.theme.EliminatedAvatar
import com.ufps.impostor.ui.theme.NeutralBadge
import com.ufps.impostor.ui.theme.PrimaryRed
import com.ufps.impostor.ui.theme.SuccessBackground
import com.ufps.impostor.ui.theme.SuccessDark
import com.ufps.impostor.ui.theme.SuccessGreen
import com.ufps.impostor.ui.theme.TextMuted

@Composable
fun ScreenEliminado(viewModel: GameViewModel, onBack: () -> Unit, onNextRound: () -> Unit) {
    val state = viewModel.state
    val eliminated = state.lastEliminated ?: return
    val wasImpostor = eliminated.isImpostor
    val civilians = state.players.count { !it.isImpostor && it.isAlive }
    val impostors = state.players.count { it.isImpostor && it.isAlive }
    val isHighDanger = civilians - impostors <= 1

    Column(modifier = Modifier.fillMaxSize()) {
        Header(subtitle = "Resultado de la Votación", onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = if (wasImpostor) "¡IMPOSTOR ELIMINADO!" else "EXPULSIÓN INJUSTIFICADA",
                style = MaterialTheme.typography.labelLarge,
                color = if (wasImpostor) SuccessDark else PrimaryRed
            )
            Text(
                text = if (wasImpostor) {
                    "¡${eliminated.name} SÍ era el Impostor!"
                } else {
                    "¡${eliminated.name} NO era el Impostor!"
                },
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = if (wasImpostor) {
                    "Los civiles han acertado en su deducción. ${eliminated.name} era un Agente Infiltrado y ha sido expulsado de la mesa."
                } else {
                    "Los civiles han cometido un grave error de deducción. ${eliminated.name} era Civil Inocente y queda fuera de la partida."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )

            NeoCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(EliminatedAvatar),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = eliminated.letter,
                            style = MaterialTheme.typography.headlineSmall,
                            color = PrimaryRed
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = eliminated.name, style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.width(8.dp))
                            Badge("Expulsado/a", background = NeutralBadge, color = TextMuted)
                        }
                        Spacer(Modifier.height(4.dp))
                        Badge(if (wasImpostor) "🎭 ROL: AGENTE INFILTRADO" else "ROL: CIVIL INOCENTE")
                    }
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    text = when {
                        !wasImpostor -> "Queda fuera de los votos y las pistas de la siguiente ronda."
                        impostors == 1 -> "¡Buen trabajo! Aún queda 1 impostor oculto en la mesa."
                        else -> "¡Buen trabajo! Aún quedan $impostors impostores ocultos en la mesa."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }

            NeoCard(background = BgCardSubtle) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "BALANCE DE LA MESA", style = MaterialTheme.typography.titleSmall)
                    Badge(
                        when {
                            wasImpostor -> "Golpe Certero"
                            isHighDanger -> "Tensión Máxima"
                            else -> "Partida en Curso"
                        }
                    )
                }
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    BalanceBox("$civilians", "CIVILES RESTANTES", Modifier.weight(1f))
                    BalanceBox("$impostors", "🎭 IMPOSTORES OCULTOS", Modifier.weight(1f), isImpostor = true)
                }
            }

            if (wasImpostor) {
                AlertBox(
                    text = "🎯 ¡Descubrieron a un infiltrado! Los civiles toman ventaja, pero la partida continúa hasta encontrar a todos.",
                    background = SuccessBackground,
                    borderColor = SuccessGreen
                )
            } else if (isHighDanger) {
                AlertBox(
                    text = "¡Peligro inminente! Si cae un civil más, los impostores igualarán la mesa y ganarán la partida inmediatamente por superioridad táctica.",
                    background = DangerBackground,
                    borderColor = DangerBorder
                )
            }

            NeoButton(text = "COMENZAR NUEVA RONDA DE PISTAS", onClick = onNextRound)
        }
    }
}

@Composable
private fun BalanceBox(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    isImpostor: Boolean = false
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(BorderStroke(1.dp, BorderDark), RoundedCornerShape(14.dp))
            .padding(vertical = 12.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            color = if (isImpostor) PrimaryRed else BorderDark
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AlertBox(text: String, background: Color, borderColor: Color) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(background)
            .border(BorderStroke(1.5.dp, borderColor), RoundedCornerShape(14.dp))
            .padding(12.dp)
    )
}
