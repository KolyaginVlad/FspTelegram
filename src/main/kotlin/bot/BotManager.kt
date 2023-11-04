package bot

import Dependencies
import bot.commands.*
import bot.constants.ConstantsKeyboards
import data.models.LockError
import dev.inmo.tgbotapi.bot.ktor.telegramBot
import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.types.ChatId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.kodein.di.instance
import repeatUntilSome
import java.time.Duration

class BotManager {
    private val startCommandProcess: StartCommandProcess by Dependencies.di.instance()
    private val checkPointProcess: CheckPointCommandProcess by Dependencies.di.instance()
    private val onCheckPointProcess: OnCheckPointRealtimeCommandProcess by Dependencies.di.instance()
    private val offCheckPointProcess: OffCheckPointRealtimeCommandsProcess by Dependencies.di.instance()
    private val scope: CoroutineScope by Dependencies.di.instance()
    private val addDataBaseCommandProcess: AddDataBaseCommandProcess by Dependencies.di.instance()
    private val processInlineButtons: ProcessInlineButtons by Dependencies.di.instance()
    private val jsonParser: Json by Dependencies.di.instance()

    init {
        val bot = telegramBot("6597360611:AAFZQQfRVjVkOZAT1_Ux6z9atoCHeWNFqmo")
        runBlocking {
            scope.launch {
                val consumer by Dependencies.di.instance<KafkaConsumer<String, String>>()
                consumer.subscribe(listOf(Dependencies.TOPIC))
                while (true) {
                    val message = repeatUntilSome {
                        consumer.poll(Duration.ofMillis(1000))
                    }
                    message.forEach {
                        val value = it.value().toString()
                        val json = jsonParser.parseToJsonElement(value)
                        when (json.jsonObject["MessageType"]?.toString()) {
                            "\"LockStatus\"" -> {
                                val lockError: LockError = jsonParser.decodeFromString(value)
                                lockError.lockInfo.forEach { info ->
                                    bot.sendTextMessage(
                                        ChatId(info.userId),
                                        """
                                    Обнаружен deadlock в базе данных ${info.database}!
                                    PID: ${info.pid}
                                    Последнее время обновления состояния: ${info.lastChangeDate}
                                    """.trimIndent(),
                                        replyMarkup = ConstantsKeyboards.repairTransactionKeyboard(
                                            info.database,
                                        )
                                    )

                                }
                            }

                            else -> {
                                println("Unresolved error ${json.jsonObject["MessageType"]?.toString()}")
                            }
                        }
                    }
                    consumer.commitAsync()
                }
            }
            bot.buildBehaviourWithLongPolling {
                println(getMe())
                startCommandProcess.start(this)
                //checkPointProcess.start(this)
                onCheckPointProcess.start(this)
                offCheckPointProcess.start(this)
                //addDataBaseCommandProcess.start(this)
                processInlineButtons.start(this)
            }.join()
        }
    }
}