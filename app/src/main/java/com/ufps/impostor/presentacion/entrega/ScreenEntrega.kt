package com.ufps.impostor.presentacion.entrega

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ufps.impostor.domain.model.GameWord
import com.ufps.impostor.domain.model.Player
import com.ufps.impostor.presentacion.components.Badge
import com.ufps.impostor.presentacion.components.Header
import com.ufps.impostor.presentacion.components.NeoButton
import com.ufps.impostor.presentacion.components.NeoCard
import com.ufps.impostor.presentacion.components.neoShadow
import com.ufps.impostor.presentacion.game.GameViewModel
import com.ufps.impostor.ui.theme.BorderDark
import com.ufps.impostor.ui.theme.IdleDot
import com.ufps.impostor.ui.theme.ImpostorCard
import com.ufps.impostor.ui.theme.NeutralBadge
import com.ufps.impostor.ui.theme.PrimaryRed
import com.ufps.impostor.ui.theme.TextMuted

@Composable
fun ScreenEntrega(viewModel: GameViewModel, onBack: () -> Unit, onAllRevealed: () -> Unit) {
    val state = viewModel.state
    val player = state.players.getOrNull(state.currentTurnIndex) ?: return
    val secretWord = state.secretWord ?: return
    val isLastPlayer = state.currentTurnIndex == state.players.size - 1

    BackHandler(onBack = onBack)

    Column(modifier = Modifier.fillMaxSize()) {
        Header(subtitle = "Turno de Entrega Secreto", onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Badge(
                text = "JUGADOR ${state.currentTurnIndex + 1} DE ${state.players.size}",
                background = NeutralBadge,
                color = TextMuted
            )
            Spacer(Modifier.height(8.dp))
            ProgressDots(total = state.players.size, current = state.currentTurnIndex)
            Spacer(Modifier.height(12.dp))
            Text(
                text = if (state.isHolding) "CONFIDENCIAL EN PANTALLA" else "TURNO DE ENTREGA",
                style = MaterialTheme.typography.labelLarge,
                color = TextMuted
            )
            Text(text = "Pásale el teléfono a", style = MaterialTheme.typography.headlineSmall)
            Text(
                text = player.name,
                style = MaterialTheme.typography.headlineMedium,
                color = PrimaryRed
            )
            Spacer(Modifier.height(14.dp))

            // La altura fija evita que el diseño salte al mostrar u ocultar el secreto
            Box(modifier = Modifier.height(295.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                when {
                    !state.isHolding -> CoveredCard(player.name)
                    player.isImpostor -> ImpostorContent(player, secretWord)
                    else -> CivilContent(secretWord)
                }
            }

            Spacer(Modifier.height(16.dp))
            HoldButton(
                isHolding = state.isHolding,
                onPress = viewModel::onRevealPressed,
                onRelease = viewModel::onRevealReleased
            )

            if (state.hasRevealed && !state.isHolding) {
                Spacer(Modifier.height(12.dp))
                NeoButton(
                    text = if (isLastPlayer) "Comenzar ronda de pistas" else "Ya la vi, pasar al siguiente",
                    primary = false,
                    onClick = {
                        if (viewModel.nextTurn()) onAllRevealed()
                    }
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = if (state.hasRevealed) {
                    "✅ Identidad visualizada. Entrega el teléfono o pulsa siguiente."
                } else {
                    "Presiona y no sueltes para leer tu rol en privado."
                },
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ProgressDots(total: Int, current: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        repeat(total) { index ->
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(if (index == current) 24.dp else 8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        when {
                            index == current -> PrimaryRed
                            index < current -> TextMuted
                            else -> IdleDot
                        }
                    )
            )
        }
    }
}

@Composable
private fun CoveredCard(playerName: String) {
    NeoCard(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Filled.Lock, contentDescription = null, tint = PrimaryRed, modifier = Modifier.size(56.dp))
            Spacer(Modifier.height(14.dp))
            Text(text = "CONTENIDO CONFIDENCIAL", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Solo $playerName debe mantener presionado el botón para mirar su secreto.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun CivilContent(secretWord: GameWord) {
    NeoCard(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Badge("ERES CIVIL", background = NeutralBadge, color = BorderDark)
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Tema: ${secretWord.topic}",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )
            Spacer(Modifier.height(16.dp))
            Text(text = "PALABRA CLAVE", style = MaterialTheme.typography.labelLarge, color = TextMuted)
            Spacer(Modifier.height(4.dp))
            Text(
                text = secretWord.word,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ImpostorContent(player: Player, secretWord: GameWord) {
    NeoCard(
        modifier = Modifier.fillMaxSize(),
        background = ImpostorCard,
        borderColor = PrimaryRed
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "ERES EL IMPOSTOR",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(PrimaryRed)
                    .padding(vertical = 10.dp)
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Tema: ${secretWord.topic}",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )
            Spacer(Modifier.height(14.dp))
            Text(text = "PISTA DE CAMUFLAJE", style = MaterialTheme.typography.labelLarge, color = PrimaryRed)
            Spacer(Modifier.height(4.dp))
            Text(
                text = player.hint.orEmpty(),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun HoldButton(isHolding: Boolean, onPress: () -> Unit, onRelease: () -> Unit) {
    val shape = RoundedCornerShape(14.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .neoShadow(3.dp, 14.dp, if (isHolding) BorderDark else PrimaryRed)
            .clip(shape)
            .background(if (isHolding) PrimaryRed else BorderDark)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        onPress()
                        // El finally oculta el secreto aunque el gesto se cancele
                        try {
                            tryAwaitRelease()
                        } finally {
                            onRelease()
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isHolding) "SUELTA PARA OCULTAR" else "Mantén presionado para ver",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )
    }
}
