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
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.streams.StreamsConfig
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import org.sour.cabbage.soup.consumer

object Dependencies {
    val di = DI {
        bindSingleton<Api> { HttpRequester(instance()) }

        bindSingleton { CoroutineScope(Dispatchers.IO + SupervisorJob()) }

        bindSingleton { CheckPointDateCommandProcess(instance()) }

        bindSingleton { StartCommandProcess(instance()) }

        bindSingleton { CheckPointCommandProcess(instance()) }

        bindSingleton { AddDataBaseCommandProcess(instance()) }

        bindSingleton { OnCheckPointRealtimeCommandProcess(instance()) }

        bindSingleton { OffCheckPointRealtimeCommandsProcess() }

        bindSingleton { ProcessInlineButtons(instance()) }
        bindSingleton { MetrixCommand(instance()) }

        bindSingleton { ProcessCustomQuery() }

//        bindSingleton {
//            val streams = kafkaStreams(
//                KafkaStreamsConfig(
//                    topologyBuilder = topology(),
//                    streamsConfig = streamsConfig(),
//                    builder = StreamsBuilder()
//                )
//            )
//            streams.cleanUp()
//            streams.start()
//
//            streams
//        }

        bindSingleton {
            consumer<String, String>(
                mapOf(
                    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to BOOTSTRAP_SERVERS_CONFIG,
                    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                    ConsumerConfig.GROUP_ID_CONFIG to "0",
//                    ConsumerConfig.CLIENT_ID_CONFIG to "amazing-consumer-client"
                ),
                listOf(TOPIC)
            )
        }

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

    fun streamsConfig(): Map<String, Any> {
        return mapOf(
            StreamsConfig.BOOTSTRAP_SERVERS_CONFIG to BOOTSTRAP_SERVERS_CONFIG,
            StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG to Serdes.String().javaClass,
            StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG to Serdes.String().javaClass,
            StreamsConfig.APPLICATION_ID_CONFIG to "amazing-app",
            StreamsConfig.CLIENT_ID_CONFIG to "amazing-client",
            StreamsConfig.COMMIT_INTERVAL_MS_CONFIG to 1000,
        )
    }

    const val BOOTSTRAP_SERVERS_CONFIG = "http://188.225.46.50:9092"
    const val TOPIC = "log-topic"
}