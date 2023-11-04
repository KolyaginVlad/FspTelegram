package data.models

import kotlinx.serialization.Serializable

@Serializable
data class GetListDatabaseDto(
    val credentialsList: List<DataBaseResponseDto>,
    val error: String? = null
)