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