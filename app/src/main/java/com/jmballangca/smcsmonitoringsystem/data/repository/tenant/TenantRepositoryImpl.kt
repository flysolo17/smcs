package com.jmballangca.smcsmonitoringsystem.data.repository.tenant

import com.google.firebase.firestore.FirebaseFirestore
import com.jmballangca.smcsmonitoringsystem.data.model.Tenant
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TenantRepositoryImpl(
    private val firestore: FirebaseFirestore
): TenantRepository {
    private val tenantCollection = firestore.collection("tenants")
    override suspend fun createTenant(tenant: Tenant) : Result<String> {
        return try {
            val documentReference = tenantCollection.add(tenant).await()
            tenantCollection.document(documentReference.id).update("id", documentReference.id).await()
            Result.success("New Tenant created!")
        } catch (e: Exception) {
            Result.failure(e)

        }
    }

    override fun getAllTenants(): Flow<List<Tenant>> {
        return callbackFlow {
            val listenerRegistration = tenantCollection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val tenants = snapshot?.toObjects(Tenant::class.java) ?: emptyList()
                trySend(tenants)
            }
            awaitClose { listenerRegistration.remove() }
        }
    }

    override suspend fun deleteTenant(id: String): Result<String> {
        return try {
            tenantCollection.document(id).delete().await()
            Result.success("Tenant deleted!")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}