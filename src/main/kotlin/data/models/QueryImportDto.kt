package data.models

import kotlinx.serialization.Serializable

@Serializable
data class QueryImportDto(
    val name: String,
    val query: String
)