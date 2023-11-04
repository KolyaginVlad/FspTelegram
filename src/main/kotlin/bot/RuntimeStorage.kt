package bot

import java.util.*

object RuntimeStorage {
    val userRealtimeMap = Collections.synchronizedMap(mutableMapOf<Long, Boolean>())
    val userData = Collections.synchronizedList(mutableListOf<User>())
}

data class User(
    val id : Long,
    var currentDb: String,
)