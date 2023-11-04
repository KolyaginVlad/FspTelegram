
import bot.BotManager
import io.ktor.server.application.*
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.sour.cabbage.soup.Kafka

tailrec fun <T> repeatUntilSome(block: () -> T?): T = block() ?: repeatUntilSome(block)

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
}