package com.jmballangca.smcsmonitoringsystem.data.repository.auth

import com.jmballangca.smcsmonitoringsystem.data.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {



    fun getUserById(id : String) : Flow<User?>

    suspend fun createMRFChecker(
        user : User
    ) : Result<String?>


    fun getAllMRFChecker() : Flow<List<User>>

    suspend fun deleteMRFChecker(id : String) : Result<String>

    suspend fun login(
        username : String,
        password : String
    ) : Result<User?>

    suspend fun logout() : Result<String>

    suspend fun getCurrentUser() : Result<User?>

     fun listenToCurrentUser() : Flow<User?>

}