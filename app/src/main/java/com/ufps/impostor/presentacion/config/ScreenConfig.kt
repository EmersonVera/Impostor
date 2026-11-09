package com.ufps.impostor.presentacion.config

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ufps.impostor.domain.use_case.StartGameUseCase
import com.ufps.impostor.presentacion.components.Badge
import com.ufps.impostor.presentacion.components.Header
import com.ufps.impostor.presentacion.components.NeoButton
import com.ufps.impostor.presentacion.components.NeoCard
import com.ufps.impostor.presentacion.components.PlayerAvatar
import com.ufps.impostor.presentacion.game.GameViewModel
import com.ufps.impostor.ui.theme.BgCardSubtle
import com.ufps.impostor.ui.theme.BorderDark
import com.ufps.impostor.ui.theme.BorderLight
import com.ufps.impostor.ui.theme.NeutralBadge
import com.ufps.impostor.ui.theme.PrimaryRed
import com.ufps.impostor.ui.theme.TextMuted

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ScreenConfig(viewModel: GameViewModel, onBack: () -> Unit, onStart: () -> Unit) {
    val state = viewModel.state
    val playerCount = state.playerNames.size
    val minPlayers = StartGameUseCase.MIN_PLAYERS
    val canStart = playerCount >= minPlayers

    Column(modifier = Modifier.fillMaxSize()) {
        Header(subtitle = "Lobby", onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(text = "Configuración de partida", style = MaterialTheme.typography.headlineMedium)
            Text(
                text = "Ajusta los participantes e impostores para esta ronda.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )

            NeoCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Participantes", style = MaterialTheme.typography.titleMedium)
                    Badge(
                        text = "$playerCount ${if (playerCount == 1) "añadido" else "añadidos"}",
                        background = NeutralBadge,
                        color = TextMuted
                    )
                }
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = state.nameInput,
                        onValueChange = viewModel::onNameInputChange,
                        placeholder = {
                            Text(
                                "Escribe un nombre (ej. Sofía)...",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        singleLine = true,
                        isError = state.nameError != null,
                        shape = RoundedCornerShape(14.dp),
                        textStyle = MaterialTheme.typography.bodyMedium,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BorderDark,
                            unfocusedBorderColor = BorderLight
                        ),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { viewModel.addPlayer() }),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    NeoButton(
                        text = "Añadir",
                        onClick = viewModel::addPlayer,
                        modifier = Modifier.width(96.dp)
                    )
                }
                if (state.nameError != null) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = state.nameError,
                        style = MaterialTheme.typography.bodySmall,
                        color = PrimaryRed
                    )
                }
                Spacer(Modifier.height(12.dp))
                if (state.playerNames.isEmpty()) {
                    Text(text = "Aún no hay participantes en la lista.", style = MaterialTheme.typography.titleSmall)
                    Text(
                        text = "Escribe un nombre y pulsa Añadir (mínimo 3 jugadores).",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                } else {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        state.playerNames.forEachIndexed { index, name ->
                            PlayerChip(name, index, onRemove = { viewModel.removePlayer(name) })
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    text = if (canStart) {
                        "¡Mesa lista! Puedes seguir añadiendo más participantes"
                    } else {
                        "Mínimo 3 jugadores para iniciar (faltan ${minPlayers - playerCount})"
                    },
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
                    Text(text = "🎭 Número de impostores", style = MaterialTheme.typography.titleMedium)
                    Badge("Infiltrados")
                }
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StepperButton(
                        text = "−",
                        description = "Disminuir impostores",
                        enabled = state.impostorCount > 1,
                        onClick = viewModel::decreaseImpostors
                    )
                    Text(
                        text = impostorLabel(state.impostorCount),
                        style = MaterialTheme.typography.titleLarge
                    )
                    StepperButton(
                        text = "+",
                        description = "Aumentar impostores",
                        enabled = state.impostorCount < state.maxImpostors && canStart,
                        onClick = viewModel::increaseImpostors
                    )
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    text = if (canStart) {
                        "${impostorLabel(state.impostorCount)} en esta sala"
                    } else {
                        "Agrega al menos 3 jugadores para configurar infiltrados"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }

            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = PrimaryRed
                )
            }
            NeoButton(text = "Comenzar partida", onClick = onStart, enabled = canStart)
            Text(
                text = if (canStart) {
                    "La pantalla se bloqueará para el primer jugador al pulsar iniciar."
                } else {
                    "Añade al menos 3 jugadores para poder iniciar la partida."
                },
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

private fun impostorLabel(count: Int): String {
    return "$count ${if (count == 1) "impostor" else "impostores"}"
}

@Composable
private fun PlayerChip(name: String, index: Int, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(BgCardSubtle)
            .border(BorderStroke(1.dp, BorderLight), RoundedCornerShape(50))
            .padding(start = 4.dp, end = 6.dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlayerAvatar(letter = name.first().uppercase(), playerIndex = index, size = 26.dp)
        Spacer(Modifier.width(6.dp))
        Text(text = name, style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.width(4.dp))
        Icon(
            Icons.Filled.Close,
            contentDescription = "Quitar a $name",
            tint = TextMuted,
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .clickable(onClick = onRemove)
        )
    }
}

@Composable
private fun StepperButton(
    text: String,
    description: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .alpha(if (enabled) 1f else 0.4f)
            .clip(RoundedCornerShape(12.dp))
            .border(BorderStroke(2.dp, BorderDark), RoundedCornerShape(12.dp))
            .clickable(enabled = enabled, onClickLabel = description, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = MaterialTheme.typography.headlineSmall)
    }
}
