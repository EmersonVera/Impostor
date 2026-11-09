package com.ufps.impostor.domain.use_case

class GetMaxImpostorsUseCase {
    // Los impostores siempre deben ser menos que los civiles
    fun invocar(playerCount: Int): Int {
        return maxOf(1, (playerCount - 1) / 2)
    }
}
