package data.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateQueryExportDto(
    val userId: Long,
    val linkName: String,
    val databaseName: String,
    val query: String
)
