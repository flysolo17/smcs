package com.jmballangca.smcsmonitoringsystem.data.repository.auth

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.jmballangca.smcsmonitoringsystem.data.model.User
import com.jmballangca.smcsmonitoringsystem.data.model.UserRole
import com.jmballangca.smcsmonitoringsystem.data.security.CryptoUtil
import com.jmballangca.smcsmonitoringsystem.data.utils.SessionManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
class AuthRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager,
) : AuthRepository {

    private val usersCollection = firestore.collection("users")


    override suspend fun getCurrentUser(): Result<User?> {
        return try {
            val uid = sessionManager.getUid()
            if (uid == null) {
                return Result.success(null)
            }
            val userSnapshot = usersCollection.document(uid).get().await()
            val user = userSnapshot.toObject(User::class.java)
            delay(1000)
            Result.success(user)
        } catch (e : Exception) {
            Result.failure(e)
        }
    }

    override  fun listenToCurrentUser(): Flow<User?> {
        return callbackFlow {
            val uid = sessionManager.getUid()
            if (uid == null) {

                trySend(null)
                close()
                return@callbackFlow
            }
            val currentUserId = usersCollection.document(uid)

            val listenerRegistration = currentUserId
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        close(e)
                        return@addSnapshotListener
                    }
                    val user = snapshot?.toObject(User::class.java)
                    trySend(user)
                }

            awaitClose { listenerRegistration.remove() }
        }
    }

    override fun getAllMRFChecker(): Flow<List<User>> = callbackFlow {
        val listenerRegistration = usersCollection
            .whereEqualTo("role", UserRole.MRF_CHECKER.name)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error fetching MRF Checkers: ${error.message}", error)
                    close(error)
                    return@addSnapshotListener
                }

                val users = snapshot?.toObjects(User::class.java) ?: emptyList()
                Log.d(TAG, "Fetched ${users.size} MRF Checkers")
                trySend(users).isSuccess
            }

        awaitClose { listenerRegistration.remove() }
    }

    override fun getUserById(id: String): Flow<User?> = callbackFlow {
        val listenerRegistration = usersCollection
            .document(id)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error fetching user by ID $id: ${error.message}", error)
                    close(error)
                    return@addSnapshotListener
                }

                val user = snapshot?.toObject(User::class.java)
                Log.d(TAG, "Fetched user: ${user?.username} (ID: ${user?.id})")
                trySend(user).isSuccess
            }

        awaitClose { listenerRegistration.remove() }
    }

    override suspend fun createMRFChecker(user: User): Result<String?> {
        return try {
            val querySnapshot = usersCollection
                .whereEqualTo("username", user.username)
                .limit(1)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                return Result.failure(Exception("User already exists"))
            }

            val encryptedPassword = CryptoUtil.encrypt(user.password)
            val userWithLogger = user.copy(
                password = encryptedPassword,
                createdAt = System.currentTimeMillis()
            )
            val docRef = usersCollection.add(userWithLogger).await()
            usersCollection.document(docRef.id).update("id", docRef.id).await()
            Log.d(TAG, "MRF Checker created with ID: ${docRef.id}")
            Result.success(docRef.id)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create MRF Checker: ${e.message}", e)
            Result.failure(e)
        }
    }


    override suspend fun deleteMRFChecker(id: String): Result<String> {
        return try {
            usersCollection.document(id).delete().await()
            Log.d(TAG, "MRF Checker with ID $id deleted successfully")
            Result.success("Deleted successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete MRF Checker with ID $id: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun login(username: String, password: String): Result<User?> {
        return try {
            val encryptedPassword = CryptoUtil.encrypt(password)
            Log.d(TAG, "Login attempt for username: $encryptedPassword")

            val querySnapshot = usersCollection
                .whereEqualTo("username", username)
                .whereEqualTo("password", encryptedPassword)
                .limit(1)
                .get()
                .await()

            val user = querySnapshot.documents.firstOrNull()?.toObject(User::class.java)
            Log.d(TAG,user.toString())
            return if (user != null) {
                Log.d(TAG, "Login successful for user: ${user.username}")
                usersCollection.document(user.id).update("online", true).await()
                sessionManager.saveUid(user.id)
                Result.success(user)
            } else {
                Log.d(TAG, "Login failed: Invalid username or password for $username")
                Result.failure(Exception("Invalid username or password"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Login error: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<String> {
        return try {
            val uid = sessionManager.getUid() ?: throw Exception("User not logged in")
            usersCollection.document(uid).update("online", false).await()
            sessionManager.clearUid()
            Log.d(TAG, "Logout successful")
            Result.success("Logout successful")
        } catch (e: Exception) {
            Log.e(TAG, "Logout error: ${e.message}", e)
            Result.failure(e)
        }
    }


    companion object {
        const val TAG = "user"
    }
}

