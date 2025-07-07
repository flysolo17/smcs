package com.jmballangca.smcsmonitoringsystem.data.repository.waste

import androidx.paging.Pager
import androidx.paging.PagingSource
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.jmballangca.smcsmonitoringsystem.data.model.WasteLog
import kotlinx.coroutines.flow.Flow

interface WasteLogRepository {

    suspend fun addWasteLog(wasteLog: WasteLog) : Result<String>

    fun getAllWasteLog(
        startAt: Long = System.currentTimeMillis(),
        endAt: Long = System.currentTimeMillis(),
        limit: Int = 1000,
    ) : Flow<List<WasteLog>>

    fun getRecentWasteLog(
        limit: Int = 10,
    ) : Flow<List<WasteLog>>

    fun getPaginatedWasteLog(
        startAt: Long = System.currentTimeMillis(),
        endAt: Long = System.currentTimeMillis(),
        limit: Int = 10,
        direction : Query.Direction = Query.Direction.DESCENDING
    ) : Pager<QuerySnapshot, WasteLog>

    suspend fun getWasteLogReport(
        startDate: Long,
        endDate: Long
    ) : Result<List<WasteLog>>

    fun getWasteLogByChecker(
        startAt: Long = System.currentTimeMillis(),
        endAt: Long = System.currentTimeMillis(),
        limit: Int = 10,
        direction : Query.Direction = Query.Direction.DESCENDING
    ) : Pager<QuerySnapshot, WasteLog>
}