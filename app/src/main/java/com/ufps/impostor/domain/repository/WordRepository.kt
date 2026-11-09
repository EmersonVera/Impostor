package com.ufps.impostor.domain.repository

import com.ufps.impostor.domain.model.GameWord

interface WordRepository {
    suspend fun getWords(): Result<List<GameWord>>
}
