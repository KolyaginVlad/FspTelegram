package bot

import java.util.*

object RuntimeStorage {
    val userRealtimeMap = Collections.synchronizedMap(mutableMapOf<Long, Boolean>())
}