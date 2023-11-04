package data

import data.models.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class HttpRequester(private val client: HttpClient) : Api {
    override suspend fun sendConfig(
        userId: Long,
        name: String,
        host: String,
        port: String,
        database: String,
        username: String,
        password: String
    ): Result<Unit> {
        println("sendConfig $userId $host $port $database $username $password")
        val response = runCatching {
            client.post("${BASE_URL}credentials") {
                contentType(ContentType.Application.Json)
                setBody(
                    ConfigExportDto(
                        userId = userId,
                        host = host,
                        name = name,
                        port = port,
                        database = database,
                        username = username,
                        password = password
                    )
                )
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
            client.get("${BASE_URL}Activity/get-error-stats/$userId/$dataBase") {
                contentType(ContentType.Application.Json)
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("checkPoint ${response?.status?.value}")
        return if (response?.status?.value in 200..299) {
            Result.success(Unit)
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun checkPointOnDate(userId: Long, dataBase: String, date: String): Result<Unit> {
        val response = runCatching {
            client.post("TODO()") {
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

    override suspend fun getDataBaseList(userId: Long): Result<List<DataBaseResponseDto>> {
        val response = runCatching {
            client.get(BASE_URL.plus("credentials/$userId")) {
                contentType(ContentType.Application.Json)
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("getDataBaseList ${response?.status?.value} ${response?.body<String>()}")
        val body = response?.body<GetListDatabaseDto>()
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(body?.credentialsList.orEmpty())
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun killTransaction(userId: Long, database: String): Result<Unit> {
        val response = runCatching {
            client.get("${BASE_URL}Activity/kill-transaction/$userId/$database") {
                contentType(ContentType.Application.Json)
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("killTransaction ${response?.status?.value}")
        return if (response?.status?.value in 200..299) {
            Result.success(Unit)
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun getMetrix(dataBase: String): Result<MetrixDto> {
        val response = runCatching {
            client.get("${BASE_URL}credentials/stat-database/$dataBase") {
                contentType(ContentType.Application.Json)
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("getMetrix ${response?.status?.value}")
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(response.body<MetrixDto>())
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun vacuum(userId: Long, dataBase: String): Result<VacuumDto> {
        val response = runCatching {
            client.get("${BASE_URL}Vacuum/full/$userId/$dataBase") {
                contentType(ContentType.Application.Json)
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("vacuum ${response?.status?.value}")
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(response.body())
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun link(userId: Long): Result<String> {
        val response = runCatching {
            client.get("${BASE_URL}Link/credential/$userId") {
                contentType(ContentType.Application.Json)
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("link ${response?.status?.value}")
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(response.body())
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun visual(link: String): Result<String> {
        val response = runCatching {
            client.get("${BASE_URL}/api/Visual/$link") {
                contentType(ContentType.Application.Json)
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("link ${response?.status?.value}")
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(response.body())
        } else {
            Result.failure(Exception())
        }
    }


}

const val BASE_URL = "http://188.225.46.50:81/api/"