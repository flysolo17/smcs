package com.jmballangca.smcsmonitoringsystem.di

import com.google.firebase.firestore.FirebaseFirestore
import com.jmballangca.smcsmonitoringsystem.data.repository.auth.AuthRepository
import com.jmballangca.smcsmonitoringsystem.data.repository.auth.AuthRepositoryImpl
import com.jmballangca.smcsmonitoringsystem.data.repository.haulers.HaulerRepository
import com.jmballangca.smcsmonitoringsystem.data.repository.haulers.HaulerRepositoryImpl
import com.jmballangca.smcsmonitoringsystem.data.repository.tenant.TenantRepository
import com.jmballangca.smcsmonitoringsystem.data.repository.tenant.TenantRepositoryImpl
import com.jmballangca.smcsmonitoringsystem.data.repository.waste.WasteLogRepository
import com.jmballangca.smcsmonitoringsystem.data.repository.waste.WasteLogRepositoryImpl
import com.jmballangca.smcsmonitoringsystem.data.utils.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideAuthRepository(firestore: FirebaseFirestore, sessionManager: SessionManager): AuthRepository {
        return AuthRepositoryImpl(firestore,sessionManager)
    }

    @Provides
    fun provideHaulerRepository(
        firestore: FirebaseFirestore
    ): HaulerRepository = HaulerRepositoryImpl(
        firestore = firestore
    )

    @Provides
    fun provideTenantRepository(
        firestore: FirebaseFirestore
    ): TenantRepository = TenantRepositoryImpl(
        firestore = firestore
    )

    @Provides
    fun provideWasteLogRepository(
        firestore: FirebaseFirestore,
        sessionManager: SessionManager
    ): WasteLogRepository = WasteLogRepositoryImpl(
        firestore = firestore,
        sessionManager = sessionManager
    )


}