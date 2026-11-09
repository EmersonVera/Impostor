package com.ufps.impostor.presentacion.votacion

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ufps.impostor.domain.model.Player
import com.ufps.impostor.domain.model.VoteResult
import com.ufps.impostor.presentacion.components.Badge
import com.ufps.impostor.presentacion.components.Header
import com.ufps.impostor.presentacion.components.NeoButton
import com.ufps.impostor.presentacion.components.PlayerAvatar
import com.ufps.impostor.presentacion.game.GameViewModel
import com.ufps.impostor.ui.theme.BgAccentLight
import com.ufps.impostor.ui.theme.BgCard
import com.ufps.impostor.ui.theme.BorderDark
import com.ufps.impostor.ui.theme.BorderLight
import com.ufps.impostor.ui.theme.PrimaryRed
import com.ufps.impostor.ui.theme.SuspectBadge
import com.ufps.impostor.ui.theme.TextMuted

@Composable
fun ScreenVotacion(
    viewModel: GameViewModel,
    onBack: () -> Unit,
    onVoted: (VoteResult) -> Unit
) {
    val state = viewModel.state

    Column(modifier = Modifier.fillMaxSize()) {
        Header(subtitle = "Accusation And Vote", onBack = onBack)
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(text = "¿Quién es el Impostor?", style = MaterialTheme.typography.headlineMedium)
                Text(
                    text = "Debatan las pistas y seleccionen al sospechoso para someterlo a juicio.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
            }
            items(state.players.filter { it.isAlive }) { player ->
                VoteItem(
                    player = player,
                    isSelected = player.name == state.selectedSuspect,
                    onClick = { viewModel.selectSuspect(player.name) }
                )
            }
        }
        Box(modifier = Modifier.padding(20.dp)) {
            NeoButton(
                text = "Confirmar voto",
                enabled = state.selectedSuspect != null,
                onClick = {
                    val result = viewModel.confirmVote()
                    if (result != null) onVoted(result)
                }
            )
        }
    }
}

@Composable
private fun VoteItem(player: Player, isSelected: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(14.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(if (isSelected) BgAccentLight else BgCard)
            .border(BorderStroke(2.dp, if (isSelected) PrimaryRed else BorderLight), shape)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlayerAvatar(player.letter, player.id - 1)
        Spacer(Modifier.width(12.dp))
        Text(
            text = player.name,
            style = MaterialTheme.typography.titleMedium,
            color = BorderDark,
            modifier = Modifier.weight(1f)
        )
        if (isSelected) {
            Badge("SOSPECHOSO", background = SuspectBadge, color = Color.White)
        }
    }
}
