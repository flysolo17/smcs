package com.jmballangca.smcsmonitoringsystem.data.repository.waste

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.jmballangca.smcsmonitoringsystem.data.model.WasteLog
import com.jmballangca.smcsmonitoringsystem.data.pagination.WasteLogPagingSource
import com.jmballangca.smcsmonitoringsystem.data.utils.SessionManager
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.getEndTime
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.getStartTime
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class WasteLogRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager
): WasteLogRepository {
    val wasteLogCollection = firestore.collection("waste_logs")
    override suspend fun addWasteLog(wasteLog: WasteLog): Result<String> {
        return try {

            val document = wasteLogCollection.add(wasteLog).await()
            wasteLogCollection.document(document.id).update("id", document.id).await()
            Result.success("New Waste log Recorded.")
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override fun getAllWasteLog(
        startAt: Long,
        endAt: Long,
        limit: Int
    ): Flow<List<WasteLog>> {
        return callbackFlow {
            val listenerRegistration = wasteLogCollection
                .whereGreaterThanOrEqualTo("createdAt", startAt.getStartTime())
                .whereLessThanOrEqualTo("createdAt", endAt.getEndTime())
                .orderBy("createdAt", Query.Direction.ASCENDING)
                .limit(limit.toLong())
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        close(e) // Propagate error
                        return@addSnapshotListener
                    }

                    val wasteLogs = snapshot?.toObjects(WasteLog::class.java) ?: emptyList()
                    trySend(wasteLogs)
                }
            awaitClose {
                listenerRegistration.remove()
            }
        }
    }

    override fun getRecentWasteLog(
        limit: Int
    ): Flow<List<WasteLog>> {
        return callbackFlow {
            val uid = sessionManager.getUid()
            if (uid == null) {
                close(Exception("User not logged in"))
                return@callbackFlow
            }
            val listenerRegistration = wasteLogCollection
                .whereEqualTo("createdBy", uid)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        close(e)
                        return@addSnapshotListener
                    }

                    val wasteLogs = snapshot?.toObjects(WasteLog::class.java) ?: emptyList()
                    trySend(wasteLogs)
                }
            awaitClose {
                listenerRegistration.remove()
            }
        }
    }

    override fun getPaginatedWasteLog(
        startAt: Long,
        endAt: Long,
        limit: Int,
        direction : Query.Direction,
    ): Pager<QuerySnapshot, WasteLog> {
        val query = wasteLogCollection
            .whereGreaterThanOrEqualTo("createdAt", startAt.getStartTime())
            .whereLessThanOrEqualTo("createdAt", endAt.getEndTime())
            .orderBy("createdAt", direction)
            .limit(limit.toLong())
        return Pager(
            config = PagingConfig(pageSize = limit),
            pagingSourceFactory = { WasteLogPagingSource(query) }
        )
    }

    override suspend fun getWasteLogReport(
        startDate: Long,
        endDate: Long
    ): Result<List<WasteLog>> {
        return try {
            val snapshot = wasteLogCollection
                .whereGreaterThanOrEqualTo("createdAt", startDate.getStartTime())
                .whereLessThanOrEqualTo("createdAt", endDate.getEndTime())
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            val results = snapshot.documents.mapNotNull { it.toObject(WasteLog::class.java) }
            Result.success(results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getWasteLogByChecker(
        startAt: Long,
        endAt: Long,
        limit: Int,
        direction: Query.Direction
    ): Pager<QuerySnapshot, WasteLog> {
        val uid = sessionManager.getUid()
        if (uid == null) {
            throw Exception("User not logged in")
        }
        val query = wasteLogCollection
            .whereEqualTo("createdBy",uid)
            .whereGreaterThanOrEqualTo("createdAt", startAt.getStartTime())
            .whereLessThanOrEqualTo("createdAt", endAt.getEndTime())
            .orderBy("createdAt", direction)
            .limit(limit.toLong())
        return Pager(
            config = PagingConfig(pageSize = limit),
            pagingSourceFactory = { WasteLogPagingSource(query) }
        )
    }
}