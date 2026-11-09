package com.ufps.impostor.domain.model

data class GameWord(
    val word: String,
    val hints: List<String>,
    val topic: String
)
