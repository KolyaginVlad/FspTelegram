package data.models

import kotlinx.serialization.Serializable

@Serializable
data class ConfigExportDto(
    val host: String,
    val port: String,
    val database: String,
    val username: String,
    val password: String,
)
