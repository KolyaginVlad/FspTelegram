package data.models

import data.ZonedDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
data class LockError(
    @SerialName("MessageType")
    val type: String,

    @SerialName("Object")
    val lockInfo: LockInfoObject
)

@Serializable
data class LockInfoObject(
    @SerialName("Pid")
    val pid: Long,

    @SerialName("State")
    val state: String,

    @SerialName("StateLastChangeDate")
    @Serializable(ZonedDateTimeSerializer::class)
    val lastChangeDate: ZonedDateTime,

    @SerialName("WaitEventType")
    val eventType: String,

    @SerialName("userId")
    val userId: Long,

    @SerialName("database")
    val database: String
)