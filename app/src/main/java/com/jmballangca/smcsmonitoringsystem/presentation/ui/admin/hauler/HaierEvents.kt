package com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.hauler

import com.jmballangca.smcsmonitoringsystem.data.model.Hauler


sealed interface HaulersEvent {
    data class OnCreateHauler(val name: Hauler) : HaulersEvent
    data class OnDelete(val id: String) : HaulersEvent

}