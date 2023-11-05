package data.models

import kotlinx.serialization.Serializable

@Serializable
data class ExecuteSshQueryExportDto(
    val queryName: String,
    val userId: Long,
    val databaseName: String
)