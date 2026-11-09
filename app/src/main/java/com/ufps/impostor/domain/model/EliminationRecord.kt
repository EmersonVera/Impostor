package com.ufps.impostor.domain.model

// Permite mostrar "Eliminado en R2" en la pantalla final
data class EliminationRecord(
    val round: Int,
    val playerName: String,
    val wasImpostor: Boolean
)
