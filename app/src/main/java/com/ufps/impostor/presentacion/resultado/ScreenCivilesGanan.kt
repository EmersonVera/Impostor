package com.ufps.impostor.presentacion.resultado

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ufps.impostor.presentacion.components.Badge
import com.ufps.impostor.presentacion.components.Header
import com.ufps.impostor.presentacion.components.NeoButton
import com.ufps.impostor.presentacion.components.NeoCard
import com.ufps.impostor.presentacion.components.PlayerAvatar
import com.ufps.impostor.presentacion.game.GameViewModel
import com.ufps.impostor.ui.theme.DangerBorder
import com.ufps.impostor.ui.theme.PrimaryRed
import com.ufps.impostor.ui.theme.SuccessDark
import com.ufps.impostor.ui.theme.TextMuted

@Composable
fun ScreenCivilesGanan(viewModel: GameViewModel, onBack: () -> Unit, onPlayAgain: () -> Unit) {
    val state = viewModel.state
    val secretWord = state.secretWord ?: return
    val impostors = state.players.filter { it.isImpostor }

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
            Text(text = "VEREDICTO OFICIAL", style = MaterialTheme.typography.labelLarge, color = SuccessDark)
            Text(text = "¡Los Civiles Ganan!", style = MaterialTheme.typography.headlineLarge)

            NeoCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (impostors.size > 1) "INFILTRADOS DESENMASCARADOS" else "INFILTRADO DESENMASCARADO",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.weight(1f)
                    )
                    Badge("${impostors.size} ${if (impostors.size > 1) "IMPOSTORES" else "IMPOSTOR"}")
                }
                Spacer(Modifier.height(10.dp))
                impostors.forEach { impostor ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .border(BorderStroke(1.5.dp, DangerBorder), RoundedCornerShape(14.dp))
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
                        Badge("ERA EL IMPOSTOR")
                    }
                }
            }

            NeoCard {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "PALABRA SECRETA", style = MaterialTheme.typography.labelLarge, color = TextMuted)
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

            NeoButton(text = "JUGAR OTRA RONDA", onClick = onPlayAgain)
            NeoButton(text = "VOLVER AL MENÚ PRINCIPAL", onClick = onBack, primary = false)
        }
    }
}
