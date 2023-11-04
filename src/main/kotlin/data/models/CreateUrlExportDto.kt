package data.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateUrlExportDto(
    val userId: Long,
    val linkName: String,
    val databaseName: String,
    val url: String
)
