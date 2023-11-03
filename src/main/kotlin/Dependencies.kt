import bot.commands.*
import data.Api
import data.HttpRequester
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

object Dependencies {
    val di = DI {
        bindSingleton<Api> { HttpRequester(instance()) }

        bindSingleton { CoroutineScope(Dispatchers.IO + SupervisorJob()) }

        bindSingleton { StartCommandProcess(instance()) }

        bindSingleton { CheckPointCommandProcess(instance()) }

        bindSingleton { AddDataBaseCommandProcess(instance()) }

        bindSingleton { OnCheckPointRealtimeCommandProcess(instance()) }

        bindSingleton { OffCheckPointRealtimeCommandsProcess() }

        bindSingleton {
            HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(
                        Json {
                            ignoreUnknownKeys = true
                            encodeDefaults = true
                        }
                    )
                }
            }
        }
    }
}