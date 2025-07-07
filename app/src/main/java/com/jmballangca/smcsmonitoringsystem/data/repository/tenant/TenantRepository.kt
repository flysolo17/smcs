package com.jmballangca.smcsmonitoringsystem.data.repository.tenant

import com.jmballangca.smcsmonitoringsystem.data.model.Tenant
import kotlinx.coroutines.flow.Flow

interface TenantRepository {
    suspend fun createTenant(tenant: Tenant): Result<String>
    fun getAllTenants(): Flow<List<Tenant>>
    suspend fun deleteTenant(id: String): Result<String>
}