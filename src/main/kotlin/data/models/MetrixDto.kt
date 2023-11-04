package data.models

import kotlinx.serialization.Serializable

@Serializable
data class MetrixDto(
    val transactionCount: String,
    val getCount: String,
    val insertCount: String,
    val updateCount: String,
    val deleteCount: String,
    val returnCount: String,
    val conflictCount: String,
    val deadlockSum: String,
    val sessionTime: String,
    val sesssionCount: String,
    val sessionKilledCount: String,
    val sessionAbandonedCount: String
) {
    @Override
    override fun toString() =
        "transactionCount $transactionCount \ngetCount $getCount  \ninsertCount $insertCount \n " +
                "updateCount $updateCount \ndeleteCount $deleteCount \nreturnCount $returnCount \n " +
                "conflictCount $conflictCount \ndeadlockSum $deadlockSum \nsessionTime $sessionTime \n" +
                "sesssionCount $sesssionCount \nsessionKilledCount $sessionKilledCount \nsessionAbandonedCount $sessionAbandonedCount"
}

/*
"transactionCount": "3208",
  "getCount": "31542",
  "insertCount": "0",
  "updateCount": "0",
  "deleteCount": "0",
  "returnCount": "220659",
  "conflictCount": "0",
  "deadlockSum": "0",
  "sessionTime": "20025309.136",
  "sesssionCount": "74",
  "sessionKilledCount": "0",
  "sessionAbandonedCount": "0"
 */