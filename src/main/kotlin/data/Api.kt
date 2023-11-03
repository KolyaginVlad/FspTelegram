package data

interface Api {

    suspend fun sendConfig(host: String, port: String, database: String, username: String, password: String)
}