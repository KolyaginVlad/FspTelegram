package data.models

import kotlinx.serialization.Serializable

@Serializable
data class ConnectionDto(
    val userId: Long,
    val ip: String,
    val port: String,
    val username: String,
    val password: String,
    val credentialId: Long
)