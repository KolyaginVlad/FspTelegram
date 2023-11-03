package data

interface Api {

    suspend fun sendConfig(
        userId: Long,
        host: String,
        port: String,
        database: String,
        username: String,
        password: String
    ): Result<Unit>

     suspend fun checkPoint(userId: Long, dataBase: String): Result<Unit>
    suspend fun checkPointOnDate(userId: Long, dataBase: String, date: String): Result<Unit>
}