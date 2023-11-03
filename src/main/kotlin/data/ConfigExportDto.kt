package data

import java.io.Serializable

data class ConfigExportDto(
    val host: String,
    val port: String,
    val database: String,
    val username: String,
    val password: String,
): Serializable
