package data

import data.models.DataBaseResponseDto
import data.models.MetrixDto


interface Api {

    suspend fun sendConfig(
        userId: Long,
        name: String,
        host: String,
        port: String,
        database: String,
        username: String,
        password: String
    ): Result<Unit>

    suspend fun checkPoint(userId: Long, dataBase: String): Result<Unit>
    suspend fun checkPointOnDate(userId: Long, dataBase: String, date: String): Result<Unit>

    suspend fun getDataBaseList(userId: Long): Result<List<DataBaseResponseDto>>

    suspend fun killTransaction(userId: Long): Result<Unit>

    suspend fun getMetrix(dataBase: String): Result<MetrixDto>

    suspend fun vacuum(userId: Long, dataBase: String) : Result<Unit>

}
