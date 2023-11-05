package data.models

import kotlinx.serialization.Serializable

@Serializable
data class SshQueryImportDto(
    val queryName: String,
    val query: String
)