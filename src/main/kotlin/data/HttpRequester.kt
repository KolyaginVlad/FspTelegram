package data

import io.ktor.client.*
import io.ktor.client.request.*

class HttpRequester(private val client: HttpClient) : Api {
    override suspend fun sendConfig(host: String, port: String, database: String, username: String, password: String) {
        client.post("http://bestserver.com/config") {
            setBody(ConfigExportDto(host, port, database, username, password))
        }
    }


}