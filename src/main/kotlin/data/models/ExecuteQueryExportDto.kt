package data.models

import kotlinx.serialization.Serializable

@Serializable
data class ExecuteQueryExportDto(
    val sql: String,
    val userId: Long,
    val databaseName: String
)