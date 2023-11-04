package data.models

import kotlinx.serialization.Serializable

@Serializable
data class ConfigExportDto(
    val userId: Long,
    val host: String,
    val name: String,
    val port: String,
    val database: String,
    val username: String,
    val password: String,
)
