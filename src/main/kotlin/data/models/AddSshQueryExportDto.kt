package data.models

import kotlinx.serialization.Serializable

@Serializable
data class AddSshQueryExportDto(
    val query: String,
    val queryName: String,
    val userId: Long,
    val databaseName: String,
)