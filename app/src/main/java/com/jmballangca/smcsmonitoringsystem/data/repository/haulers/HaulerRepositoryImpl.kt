package com.jmballangca.smcsmonitoringsystem.data.repository.haulers

import com.google.firebase.firestore.FirebaseFirestore
import com.jmballangca.smcsmonitoringsystem.data.model.Hauler
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class HaulerRepositoryImpl(
    private val firestore: FirebaseFirestore
): HaulerRepository {
    private val haulerCollection = firestore.collection("haulers")
    override suspend fun createHauler(hauler: Hauler): Result<String> {
        return try {
            val documentReference = haulerCollection.add(hauler).await()
            haulerCollection.document(documentReference.id).update("id", documentReference.id).await()
            delay(1000)
            Result.success("New Hauler created!")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAllHaulers(): Flow<List<Hauler>> {
        return callbackFlow {
            val listenerRegistration = haulerCollection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val haulers = snapshot?.toObjects(Hauler::class.java) ?: emptyList()
                trySend(haulers)
            }
            awaitClose { listenerRegistration.remove() }
        }
    }

    override suspend fun deleteHaulers(id: String): Result<String> {
        return try {
            haulerCollection.document(id).delete().await()
            delay(1000)
            Result.success("Hauler deleted!")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}