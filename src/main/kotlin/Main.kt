
import bot.BotManager
import io.ktor.server.application.*
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.sour.cabbage.soup.Kafka

fun main() {
    BotManager()
}

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    install(Kafka) {
        this.kafkaConfig = mapOf<String, Any>(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "http://188.225.46.50:9092"
        )
        this.topics = listOf(
            NewTopic("log-topic", 1, 1)
        )
    }

//    val streams: KafkaStreams by Dependencies.di.instance()
//
//    environment.monitor.subscribe(ApplicationStopped) {
//        streams.close(Duration.ofSeconds(5))
//    }
}