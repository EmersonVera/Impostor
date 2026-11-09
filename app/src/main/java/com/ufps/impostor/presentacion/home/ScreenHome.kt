package com.ufps.impostor.presentacion.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ufps.impostor.R
import com.ufps.impostor.presentacion.components.Badge
import com.ufps.impostor.presentacion.components.Header
import com.ufps.impostor.presentacion.components.NeoButton
import com.ufps.impostor.presentacion.components.NeoCard
import com.ufps.impostor.ui.theme.BorderDark
import com.ufps.impostor.ui.theme.NeutralBadge
import com.ufps.impostor.ui.theme.PrimaryRed
import com.ufps.impostor.ui.theme.TextMuted

@Composable
fun ScreenHome(onPlay: () -> Unit, onRules: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Header(subtitle = "Lobby")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.impostor_logo),
                contentDescription = "Logo de El Impostor",
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(26.dp))
            )
            Spacer(Modifier.height(12.dp))
            Text(text = "El Impostor", style = MaterialTheme.typography.headlineLarge)
            Text(
                text = "Juego social de deducción cara a cara",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )
            Spacer(Modifier.height(20.dp))

            NeoCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🎭 FICHA DE PARTIDA", style = MaterialTheme.typography.titleSmall)
                    Badge("Mesa Local", background = NeutralBadge, color = TextMuted)
                }
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatBox(Icons.Filled.Person, "3+", "Jugadores", Modifier.weight(1f))
                    StatBox(Icons.Filled.Lock, "1", "Palabra Clave", Modifier.weight(1f))
                    StatBox(Icons.Filled.Info, "100%", "Deducción Pura", Modifier.weight(1f))
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Todos conocen el código secreto excepto uno. ¿Podrás descubrir al infiltrado antes de la votación?",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }

            Spacer(Modifier.height(24.dp))
            NeoButton(text = "Jugar", onClick = onPlay, icon = Icons.Filled.PlayArrow)
            Spacer(Modifier.height(12.dp))
            NeoButton(text = "CÓMO SE JUEGA", onClick = onRules, primary = false)
        }
    }
}

@Composable
private fun StatBox(icon: ImageVector, value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .border(BorderStroke(1.dp, BorderDark), RoundedCornerShape(14.dp))
            .padding(vertical = 10.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = null, tint = PrimaryRed, modifier = Modifier.size(18.dp))
        Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
    }
}
