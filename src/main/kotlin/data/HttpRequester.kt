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

    override suspend fun getSshQueries(userId: Long, database: String): Result<List<String>> {
        println("getSshQueries $userId $database")
        val response = runCatching {
            client.get("${BASE_URL}Ssh/get-query/$userId/$database") {
                contentType(ContentType.Application.Json)
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("getSshQueries ${response?.status?.value}")
        println(response?.body<String>())
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(response.body<List<SshQueryImportDto>>().map { it.queryName })
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun getQueries(userId: Long, database: String): Result<List<String>> {
        println("getSshQueries $userId $database")
        val response = runCatching {
            client.get("${BASE_URL}Query/$userId/$database") {
                contentType(ContentType.Application.Json)
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("getSshQueries ${response?.status?.value}")
        println(response?.body<String>())
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(response.body<List<SshQueryImportDto>>().map { it.queryName })
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun createQuery(name: String, query: String, chatId: Long, database: String): Result<Unit> {
        println("createQuery $name $query $chatId $database")
        val response = runCatching {
            client.post("${BASE_URL}Query") {
                contentType(ContentType.Application.Json)
                setBody(CreateQueryExportDto(chatId, name, database, query))
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("createQuery ${response?.status?.value}")
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(Unit)
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun createSshQuery(name: String, query: String, chatId: Long, database: String): Result<Unit> {
        println("createSshQuery $name $query $chatId $database")
        val response = runCatching {
            client.post("${BASE_URL}Ssh/add-query") {
                contentType(ContentType.Application.Json)
                setBody(AddSshQueryExportDto(query, name, chatId, database))
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("createSshQuery ${response?.status?.value}")
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(Unit)
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun getQueryByName(name: String, chatId: Long, database: String): Result<String> {
        println("getQueryByName $name $chatId $database")
        val response = runCatching {
            client.get("${BASE_URL}Query/$chatId/$database/$name") {
                contentType(ContentType.Application.Json)
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("getQueryByName ${response?.status?.value}")
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(response.body<SshQueryImportDto>().query)
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun sendCustomQuery(query: String, userId: Long, database: String): Result<Unit> {
        println("${BASE_URL}custom/$userId/$database")
        val response = runCatching {
            client.post("${BASE_URL}custom") {
                contentType(ContentType.Application.Json)
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("sendCustomQuery ${response?.status?.value}")
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(Unit)
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun sshDiskSpace(userId: Long): Result<Unit> {
        val response = runCatching {
            client.get("${BASE_URL}Ssh/check-disk-space/$userId") {
                contentType(ContentType.Application.Json)
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("getQueryByName ${response?.status?.value}")
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(Unit)
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun sshLsof(userId: Long): Result<Unit> {
        val response = runCatching {
            client.get("${BASE_URL}Ssh/lsof/$userId") {
                contentType(ContentType.Application.Json)
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("getQueryByName ${response?.status?.value}")
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(Unit)
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun sshTcpdump(userId: Long): Result<Unit> {
        val response = runCatching {
            client.get("${BASE_URL}Ssh/tcpdump/$userId") {
                contentType(ContentType.Application.Json)
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("getQueryByName ${response?.status?.value}")
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(Unit)
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun addSshConnection(
        userId: Long,
        ip: String,
        port: String,
        username: String,
        password: String,
        database: String
    ): Result<Unit> {
        println(
            "${BASE_URL}Ssh/new-connection userId = $userId,\n" +
                    "                        port = $port,\n" +
                    "                        ip = $ip,\n" +
                    "                        database = $database,\n " +
                    "                        username = $username,\n" +
                    "                        password = $password"
        )
        val response = runCatching {
            client.post("${BASE_URL}Ssh/new-connection") {
                contentType(ContentType.Application.Json)
                setBody(
                    ConnectionDto(
                        userId = userId,
                        port = port,
                        ip = ip,
                        username = username,
                        password = password,
                        databaseName = database
                    )
                )
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("addSshConnection ${response?.status?.value}")
        return if (response?.status?.value in 200..299 && response != null && response.body<Boolean>()) {
            Result.success(Unit)
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun hasSshConnections(userId: Long, database: String): Result<Boolean> {
        println("${BASE_URL}Ssh/all-connections/$userId")
        val response = runCatching {
            client.get("${BASE_URL}Ssh/all-connections/$userId") {
                contentType(ContentType.Application.Json)
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("getSshConnections ${response?.status?.value}")
        println("getSshConnections ${response?.body<String>()}")
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(response.body<List<SshConnection>>().isNotEmpty())
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun removeSshQuery(chatId: Long, database: String, name: String): Result<Unit> {
        println("removeSshQuery $name $chatId $database")
        val response = runCatching {
            client.delete("${BASE_URL}Ssh/delete-query/$chatId/$database/$name") {
                contentType(ContentType.Application.Json)
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("removeSshQuery ${response?.status?.value}")
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(Unit)
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun updateSshQuery(chatId: Long, database: String, name: String, newValue: String): Result<Unit> {
        println("updateSshQuery $name $newValue $chatId $database")
        val response = runCatching {
            client.put("${BASE_URL}Ssh/update-query") {
                contentType(ContentType.Application.Json)
                setBody(AddSshQueryExportDto(newValue, name, chatId, database))
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("updateSshQuery ${response?.status?.value}")
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(Unit)
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun executeQuery(chatId: Long, database: String, sql: String): Result<Unit> {
        println("executeQuery $sql $chatId $database")
        val response = runCatching {
            client.post("${BASE_URL}Query/execute") {
                contentType(ContentType.Application.Json)
                setBody(ExecuteQueryExportDto(sql, chatId, database))
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("executeQuery ${response?.status?.value}")
        return if (response?.status?.value in 200..299 && response != null) {
            Result.success(Unit)
        } else {
            Result.failure(Exception())
        }
    }

    override suspend fun executeSshQuery(chatId: Long, database: String, name: String): Result<Unit> {
        println("executeSshQuery $name $chatId $database")
        val response = runCatching {
            client.post("${BASE_URL}Ssh/execute-template") {
                contentType(ContentType.Application.Json)
                setBody(ExecuteSshQueryExportDto(name, chatId, database))
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
        println("executeSshQuery ${response?.status?.value}")
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