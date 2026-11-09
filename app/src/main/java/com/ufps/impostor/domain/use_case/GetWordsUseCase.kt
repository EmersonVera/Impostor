package com.ufps.impostor.domain.use_case

import com.ufps.impostor.domain.model.GameWord
import com.ufps.impostor.domain.repository.WordRepository

class GetWordsUseCase(private val repository: WordRepository) {
    suspend fun invocar(): Result<List<GameWord>> {
        return repository.getWords()
    }
}
