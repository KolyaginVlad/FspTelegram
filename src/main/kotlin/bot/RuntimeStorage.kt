package bot

import java.util.Collections

object RuntimeStorage {
    val userIdList = Collections.synchronizedList(mutableListOf<Long>())
}
