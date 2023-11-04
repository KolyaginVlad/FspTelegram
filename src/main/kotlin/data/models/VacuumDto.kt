package data.models

import kotlinx.serialization.Serializable

@Serializable
data class VacuumDto(
    val beforeMemory: String,
    val afterMemory: String
)