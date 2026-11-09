package com.ufps.impostor.domain.use_case

class AddPlayerUseCase {
    // Devuelve el nombre ya limpio si se puede agregar a la lista
    fun invocar(currentNames: List<String>, name: String): Result<String> {
        val trimmed = name.trim()
        return when {
            trimmed.isEmpty() -> {
                Result.failure(Exception("Escribe un nombre"))
            }
            currentNames.any { it.equals(trimmed, ignoreCase = true) } -> {
                Result.failure(Exception("Ese nombre ya está en la lista"))
            }
            else -> Result.success(trimmed)
        }
    }
}
