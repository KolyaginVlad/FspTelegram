package data.models

import kotlinx.serialization.Serializable

@Serializable
data class CheckPointDto(
    val pid: Long?,
    val state: String?,
    val stateLastChangeDate: String?,
    val waitEventType: String?,
    val dataBase: String?,
    val messageType: String?,
    val userId: Long?
)

@Serializable
data class CheckPointOnDateDto(
    val userId: Long, val dataBase: String, val date: String
)

/*
 {
    "pid": 0,
    "state": null,
    "stateLastChangeDate": "0001-01-01T00:00:00+00:00",
    "waitEventType": null,
    "dataBase": null,
    "messageType": "Ok",
    "userId": 0
  }
  */