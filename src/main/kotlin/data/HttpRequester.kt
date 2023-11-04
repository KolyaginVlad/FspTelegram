package data

import data.models.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
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

    override suspend fun checkPoint(userId: Long, dataBase: String): Result<List<CheckPointDto>> {
        val response = runCatching {
            client.get("${BASE_URL}Activity/get-error-stats/$userId/$dataBase") {
                contentType(ContentType.Application.Json)
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("checkPoint ${response?.status?.value}")
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(response.body<List<CheckPointDto>>())
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun checkPointOnDate(userId: Long, dataBase: String, date: String): Result<Unit> {
        TODO()
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

    override suspend fun getMetrix(dataBase: String, userId: Long): Result<MetrixDto> {
        val response = runCatching {
            client.get("${BASE_URL}credentials/stat-database/$userId/$dataBase") {
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

    override suspend fun link(userId: Long, database: String): Result<List<String>> {
        val response = runCatching {
            client.get("${BASE_URL}Link/credential/$userId/$database") {
                contentType(ContentType.Application.Json)
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("link ${response?.status?.value}")
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(response.body<List<LinkImportDto>>().map { it.name })
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun createUrl(name: String, url: String, chatId: Long, database: String): Result<Unit> {
        println("createUrl $name $url $chatId $database")
        val response = runCatching {
            client.post("${BASE_URL}Link") {
                contentType(ContentType.Application.Json)
                setBody(CreateUrlExportDto(chatId, name, database, url))
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("createUrl ${response?.status?.value}")
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(Unit)
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun visual(userId: Long, database: String, link: String): Result<String> {
        println("${BASE_URL}Visual/$userId/$database/$link")
        val response = runCatching {
            client.get("${BASE_URL}Visual/$userId/$database/$link") {
                timeout {
                    requestTimeoutMillis = 30000
                }
                contentType(ContentType.Application.Json)
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("visual ${response?.status?.value}")
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(response.body())
        } else {
            Result.failure(Exception())
        }
    }


    override suspend fun connectBySsh(userId: Long, ssh: String): Result<Unit> {
        val response = runCatching {
            client.post("${BASE_URL}ssh") {
                contentType(ContentType.Application.Json)
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("connectBySsh ${response?.status?.value}")
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(Unit)
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun connectByConnectionString(userId: Long, connectionString: String): Result<Unit> {
        val response = runCatching {
            client.post("${BASE_URL}connection_string") {
                contentType(ContentType.Application.Json)
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("connectByConnectionString ${response?.status?.value}")
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(Unit)
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun changeMemory(
        userId: Long,
        dataBase: String,
        effectiveCacheSize: String,
        maintenanceWorkMemory: String,
        workMemory: String,
        memoryLimit: String
    ): Result<Unit> {
        val response = runCatching {
            client.post("${BASE_URL}Memory/memory/$userId/$dataBase") {
                contentType(ContentType.Application.Json)
                setBody(
                    MemoryResponseDto(
                        effectiveCacheSize, maintenanceWorkMemory, workMemory, memoryLimit
                    )
                )
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("changeMemory ${response?.status?.value}")
        return if (response?.status?.value in 200..299) {
            Result.success(Unit)
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun getMemory(userId: Long, dataBase: String): Result<MemoryDto> {
        val response = runCatching {
            client.get("${BASE_URL}Memory/memory/$userId/$dataBase") {
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("getMemory ${response?.status?.value}")
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(response.body())
        } else {
            Result.failure(Exception())
        }
    }
}

const val BASE_URL = "http://188.225.46.50:81/api/"