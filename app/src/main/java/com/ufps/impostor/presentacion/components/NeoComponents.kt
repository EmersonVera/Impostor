package com.ufps.impostor.presentacion.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ufps.impostor.R
import com.ufps.impostor.ui.theme.AvatarColors
import com.ufps.impostor.ui.theme.BgAccentLight
import com.ufps.impostor.ui.theme.BgCard
import com.ufps.impostor.ui.theme.BorderDark
import com.ufps.impostor.ui.theme.BorderLight
import com.ufps.impostor.ui.theme.PrimaryRed
import com.ufps.impostor.ui.theme.TextMuted

// Sombra dura desplazada, característica visual de la web
fun Modifier.neoShadow(offset: Dp, radius: Dp, color: Color = BorderDark): Modifier = drawBehind {
    drawRoundRect(
        color = color,
        topLeft = Offset(offset.toPx(), offset.toPx()),
        size = size,
        cornerRadius = CornerRadius(radius.toPx())
    )
}

@Composable
fun NeoButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    primary: Boolean = true,
    icon: ImageVector? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val shape = RoundedCornerShape(14.dp)
    val contentColor = if (primary) Color.White else BorderDark
    val press = if (isPressed && enabled) 1.dp else 0.dp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.45f)
            .offset(press, press)
            .neoShadow(if (isPressed && enabled) 1.dp else 3.dp, 14.dp)
            .clip(shape)
            .background(if (primary) PrimaryRed else BgCard)
            .border(BorderStroke(2.dp, BorderDark), shape)
            .clickable(
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .heightIn(min = 56.dp)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(10.dp))
        }
        Text(text = text, style = MaterialTheme.typography.titleMedium, color = contentColor)
    }
}

@Composable
fun NeoCard(
    modifier: Modifier = Modifier,
    background: Color = BgCard,
    borderColor: Color = BorderDark,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .neoShadow(3.dp, 20.dp)
            .clip(shape)
            .background(background)
            .border(BorderStroke(2.dp, borderColor), shape)
            .padding(18.dp),
        content = content
    )
}

@Composable
fun Badge(
    text: String,
    modifier: Modifier = Modifier,
    background: Color = BgAccentLight,
    color: Color = PrimaryRed
) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = color,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(background)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

@Composable
fun PlayerAvatar(
    letter: String,
    playerIndex: Int,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    background: Color = AvatarColors[playerIndex % AvatarColors.size]
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Text(text = letter, style = MaterialTheme.typography.titleMedium, color = Color.White)
    }
}

@Composable
fun Header(subtitle: String, onBack: (() -> Unit)? = null) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBack != null) {
                IconButton(onClick = onBack, modifier = Modifier.size(36.dp)) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Volver"
                    )
                }
                Spacer(Modifier.width(6.dp))
            }
            Image(
                painter = painterResource(R.drawable.impostor_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    text = "EL IMPOSTOR",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
        }
        HorizontalDivider(color = BorderLight)
    }
}
