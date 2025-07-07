package com.jmballangca.smcsmonitoringsystem.data.model



data class User(
    val id: String = "",
    val name: String = "",
    val username: String = "",
    val password: String = "",
    val online : Boolean = false,
    val role: UserRole = UserRole.MRF_CHECKER,
    val createdAt: Long = System.currentTimeMillis()
)

enum class UserRole {
    ADMIN, MRF_CHECKER
}
