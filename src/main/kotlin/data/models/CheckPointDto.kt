package data.models

import kotlinx.serialization.Serializable

@Serializable
data class CheckPointDto(
    val userId: Long, val dataBase: String
)

@Serializable
data class CheckPointOnDateDto(
    val userId: Long, val dataBase: String, val date: String
)

