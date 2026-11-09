package com.ufps.impostor.presentacion.reglas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import com.ufps.impostor.ui.theme.BgAccentLight
import com.ufps.impostor.ui.theme.PrimaryRed
import com.ufps.impostor.ui.theme.TextMuted

private data class RuleStep(
    val number: String,
    val phase: String,
    val title: String,
    val description: String
)

private val ruleSteps = listOf(
    RuleStep(
        "01", "INICIO", "Reparto secreto",
        "Se pasa el teléfono. Todos ven la palabra secreta salvo el impostor, quien recibe una pista general para poder fingir."
    ),
    RuleStep(
        "02", "INTERCAMBIO", "Ronda de pistas",
        "Por turnos, cada jugador dice una palabra sutil. ¡Cuidado: si eres demasiado obvio, el impostor descubrirá el enigma al instante!"
    ),
    RuleStep(
        "03", "ANÁLISIS", "Debate & sospecha",
        "Se abre la discusión libre. Analicen quién dudó en responder, quién dio una pista extraña o quién parece demasiado nervioso."
    ),
    RuleStep(
        "04", "SENTENCIA", "Votación & veredicto",
        "A la de tres, señalen al culpable. Si votan al impostor, ganan los civiles; si logra desviar las miradas, triunfa el impostor."
    )
)

@Composable
fun ScreenReglas(onBack: () -> Unit, onPlay: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Header(subtitle = "Rules Archive", onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(text = "Reglas del juego", style = MaterialTheme.typography.headlineMedium)
            Text(
                text = "Aprende la dinámica en 4 pasos esenciales y descubre al infiltrado.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )
            ruleSteps.forEach { step -> RuleCard(step) }
            NeoCard(background = BgAccentLight, borderColor = PrimaryRed) {
                Text(
                    text = "CONSEJO PRO",
                    style = MaterialTheme.typography.labelLarge,
                    color = PrimaryRed
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Como impostor, escucha atentamente las pistas iniciales de los demás para que tu palabra encaje sin desentonar en la mesa.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            NeoButton(text = "ENTENDIDO, ¡A JUGAR!", onClick = onPlay)
            Text(
                text = "Partida local • 3 o más participantes (sin límite)",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun RuleCard(step: RuleStep) {
    NeoCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = step.number,
                style = MaterialTheme.typography.headlineSmall,
                color = PrimaryRed
            )
            Spacer(Modifier.width(10.dp))
            Badge(step.phase)
        }
        Spacer(Modifier.height(6.dp))
        Text(text = step.title, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))
        Text(
            text = step.description,
            style = MaterialTheme.typography.bodyMedium,
            color = TextMuted
        )
    }
}
