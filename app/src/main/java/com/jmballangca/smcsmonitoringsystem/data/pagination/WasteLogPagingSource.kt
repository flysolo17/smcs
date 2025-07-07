package com.jmballangca.smcsmonitoringsystem.data.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.jmballangca.smcsmonitoringsystem.data.model.WasteLog
import com.jmballangca.smcsmonitoringsystem.data.repository.waste.WasteLogRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

class WasteLogPagingSource(
    private val baseQuery: Query
) : PagingSource<QuerySnapshot, WasteLog>() {

    private var lastDocumentSnapshot: DocumentSnapshot? = null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, WasteLog> {
        return try {
            val pagedQuery = lastDocumentSnapshot?.let {
                baseQuery.startAfter(it)
            } ?: baseQuery

            val snapshot = pagedQuery.get().await()
            delay(1000)

            val items = snapshot.documents.mapNotNull { it.toObject(WasteLog::class.java) }

            lastDocumentSnapshot = snapshot.documents.lastOrNull()

            LoadResult.Page(
                data = items,
                prevKey = null,
                nextKey = if (snapshot.isEmpty) null else snapshot
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<QuerySnapshot, WasteLog>): QuerySnapshot? = null
}

