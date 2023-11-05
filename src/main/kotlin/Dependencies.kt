import bot.commands.*
import data.Api
import data.HttpRequester
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import org.sour.cabbage.soup.consumer

object Dependencies {
    val di = DI {
        bindSingleton<Api> { HttpRequester(instance()) }

        bindSingleton { CoroutineScope(Dispatchers.IO + SupervisorJob()) }

        bindSingleton { CheckPointDateCommandProcess(instance()) }

        bindSingleton { KillTransactionCommandProcess(instance()) }

        bindSingleton { SshCheckDiskCommandProcess(instance()) }

        bindSingleton { SshLsofCommandProcess(instance()) }

        bindSingleton { SshTcpDumpCommandProcess(instance()) }

        bindSingleton { StartCommandProcess(instance()) }

        bindSingleton { CheckPointCommandProcess(instance()) }

        bindSingleton { ShowMemoryCommandProcess(instance()) }

        bindSingleton { ChangeMemoryCommandProcess(instance()) }

        bindSingleton { AddDataBaseCommandProcess(instance()) }

        bindSingleton { OnCheckPointRealtimeCommandProcess(instance(), instance(), instance()) }

        bindSingleton { OffCheckPointRealtimeCommandsProcess() }

        bindSingleton { ProcessInlineButtons(instance()) }

        bindSingleton { MetrixCommand(instance()) }

        bindSingleton { VacuumCommandProcess(instance()) }

        bindSingleton { ProcessCustomQuery(instance()) }

        bindSingleton { AddDatabaseSshCommandProcess(instance()) }

        bindSingleton { AddDatabaseConnectingStringCommandProcess(instance()) }

        bindSingleton { AddDatabaseFileCommandProcess() }

        bindSingleton { GetImageByLinkCommandProcess(instance()) }

        bindSingleton { GetLinksCommandProcess(instance()) }

        bindSingleton { AddLinkCommandProcess(instance()) }

        bindSingleton { SelectQueryCommandsProcess(instance()) }

        bindSingleton { ProcessQueryCommandProcess(instance(), instance()) }

        bindSingleton { AddQueryCommandProcess(instance()) }

        bindSingleton {
            consumer<String, String>(
                mapOf(
                    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to BOOTSTRAP_SERVERS_CONFIG,
                    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                    ConsumerConfig.GROUP_ID_CONFIG to "0",
                ),
                listOf(TOPIC)
            )
        }

        bindSingleton {
            HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(
                        instance<Json>()
                    )
                }
                install(HttpTimeout) {
                    requestTimeoutMillis = 10000
                }
            }
        }

        bindSingleton {
            Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
            }
        }
    }

    const val BOOTSTRAP_SERVERS_CONFIG = "http://188.225.46.50:9092"
    const val TOPIC = "log-topic"
}