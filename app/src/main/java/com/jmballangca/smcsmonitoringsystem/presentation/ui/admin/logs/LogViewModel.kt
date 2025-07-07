package com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.logs

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jmballangca.smcsmonitoringsystem.data.model.WasteLog
import com.jmballangca.smcsmonitoringsystem.data.repository.auth.AuthRepository
import com.jmballangca.smcsmonitoringsystem.data.repository.waste.WasteLogRepository
import com.jmballangca.smcsmonitoringsystem.data.utils.WasteLogHeaderData
import com.jmballangca.smcsmonitoringsystem.data.utils.WasteLogPdfGenerator
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.UiEvents
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.formatDate
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.getEndTime
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.getStartTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LogViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val wasteLogRepository: WasteLogRepository,
    private val pdfGenerator: WasteLogPdfGenerator,

) : ViewModel() {

    private val _state = MutableStateFlow(LogState())
    val state = _state.asStateFlow()

    private val _uiEventFlow = Channel<UiEvents>()
    val uiEventFlow = _uiEventFlow.receiveAsFlow()
    private val _dateRange = MutableStateFlow(Pair(state.value.startDate, state.value.endDate))


    private val _user = authRepository.listenToCurrentUser().stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )
    init {
        viewModelScope.launch {
            _user.collectLatest { user ->
                _state.value = _state.value.copy(
                    user = user
                )
            }
        }
    }

    private val startTimestamp = System.currentTimeMillis()
    private val endTimestamp = System.currentTimeMillis()
    private val pageSize = 5

    @OptIn(ExperimentalCoroutinesApi::class)
    val wasteLogsFlow: Flow<PagingData<WasteLog>> = _dateRange
        .flatMapLatest { (start, end) ->
            wasteLogRepository.getPaginatedWasteLog(
                startAt = start,
                endAt = end,
                limit = _state.value.pageSize
            ).flow
        }
        .cachedIn(viewModelScope)

    fun events(e: LogEvents) {
        when (e) {
            is LogEvents.Logout -> {
                // handle logout logic
            }

            is LogEvents.OnDateSelected -> {
                dateSelected(e.startDate, e.endDate)
            }

            is LogEvents.GenerateReport -> generateReport(e.logo)
        }
    }

    private fun generateReport(
        logo : Bitmap?
    ) {
        viewModelScope.launch {
            val startDate = state.value.startDate
            val endDate = state.value.endDate
            _state.value = _state.value.copy(
                isGeneratingReport = true
            )
            _uiEventFlow.send(
                UiEvents.ShowSuccessMessage("Generating report...")
            )
            wasteLogRepository.getWasteLogReport(
                startDate = startDate,
                endDate = endDate,
            ).onSuccess {
                val headerData = WasteLogHeaderData(
                    reportTitle = "Waste Log Report",
                    dateRange = "${startDate.formatDate()} - ${endDate.formatDate()}",
                    generatedDate = System.currentTimeMillis().formatDate(),
                    generatedBy = state.value.user?.name ?: "",
                    logoBitmap = logo,
                    totalItems = it.size,
                )
                val file = pdfGenerator.generatePdf(
                    wasteLogs = it,
                    headerData = headerData,
                    fileName = "waste_log_${System.currentTimeMillis()}"
                )
                delay(2000)
                _state.value = _state.value.copy(
                    isGeneratingReport = false
                )
                _uiEventFlow.send(UiEvents.ShowSuccessMessage("PDF generated successfully: ${file.absolutePath}"))
            }.onFailure {
                _state.value = _state.value.copy(
                    isGeneratingReport = false
                )
                _uiEventFlow.send(UiEvents.ShowErrorMessage("Error generating PDF: ${it.localizedMessage}"))
            }
        }

    }

    private fun dateSelected(startDate: Long, endDate: Long) {
        _state.value = _state.value.copy(startDate = startDate, endDate = endDate)
        _dateRange.value = Pair(startDate, endDate)
    }


}