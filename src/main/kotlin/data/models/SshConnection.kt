package data.models

import kotlinx.serialization.Serializable

@Serializable
data class SshConnection(
    val database: String
)