package com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.tenant

import com.jmballangca.smcsmonitoringsystem.data.model.Tenant


sealed interface TenantEvents {
    data class OnCreateTenant(val tenant: Tenant) : TenantEvents
    data class OnDelete(val id : String) : TenantEvents

}