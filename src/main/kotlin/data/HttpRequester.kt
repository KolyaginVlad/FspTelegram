package data

import data.models.CheckPointDto
import data.models.CheckPointOnDateDto
import data.models.ConfigExportDto
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class HttpRequester(private val client: HttpClient) : Api {
    override suspend fun sendConfig(
        userId: Long,
        host: String,
        port: String,
        database: String,
        username: String,
        password: String
    ): Result<Unit> {
        println("sendConfig $userId $host $port $database $username $password")
        val response = runCatching {
            client.post("http://188.225.46.50:81/api/Credentials") {
                contentType(ContentType.Application.Json)
                setBody(ConfigExportDto(host, port, database, username, password))
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("sendConfig ${response?.status?.value}")
        return if (response?.status?.value in 200..299) {
            Result.success(Unit)
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun checkPoint(userId: Long, dataBase: String): Result<Unit> {
        val response = runCatching {
            client.post("TODO()") {  //TODO
                contentType(ContentType.Application.Json)
                setBody(CheckPointDto(userId, dataBase))
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("sendConfig ${response?.status?.value}")
        return if (response?.status?.value in 200..299) {
            Result.success(Unit)
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun checkPointOnDate(userId: Long, dataBase: String, date: String): Result<Unit> {
        val response = runCatching {
            client.post("TODO()") { //TODO
                contentType(ContentType.Application.Json)
                setBody(CheckPointOnDateDto(userId, dataBase, date))
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("sendConfig ${response?.status?.value}")
        return if (response?.status?.value in 200..299) {
            Result.success(Unit)
        } else {
            Result.failure(Exception())
        }
    }


}