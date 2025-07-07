package com.jmballangca.smcsmonitoringsystem.data.repository.haulers

import com.jmballangca.smcsmonitoringsystem.data.model.Hauler
import kotlinx.coroutines.flow.Flow

interface HaulerRepository {
    suspend fun createHauler(
        hauler: Hauler
    ) : Result<String>

    fun getAllHaulers() : Flow<List<Hauler>>

    suspend fun deleteHaulers(id : String) : Result<String>
}