package com.jmballangca.smcsmonitoringsystem.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.jmballangca.smcsmonitoringsystem.data.utils.SessionManager
import com.jmballangca.smcsmonitoringsystem.data.utils.WasteLogPdfGenerator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideSessionManager(
        @ApplicationContext context: Context
    ) = SessionManager(context)


    @Provides
    @Singleton
    fun providePDFGenerator(@ApplicationContext context: Context) = WasteLogPdfGenerator(context)


}