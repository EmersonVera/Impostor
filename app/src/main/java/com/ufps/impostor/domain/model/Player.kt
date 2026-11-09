package com.ufps.impostor.domain.model

data class Player(
    val id: Int,
    val name: String,
    val letter: String,
    val isImpostor: Boolean = false,
    val hint: String? = null,
    val isAlive: Boolean = true
)
