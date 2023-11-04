package bot.commands

import bot.Command
import bot.RuntimeStorage
import bot.constants.ConstantsKeyboards
import data.Api
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.apache.kafka.clients.consumer.KafkaConsumer
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

class StartCommandProcess(
    private val api: Api,
    private val consumer: KafkaConsumer<String, String>
) : Command {

    tailrec fun <T> repeatUntilSome(block: () -> T?): T = block() ?: repeatUntilSome(block)

    override suspend fun BehaviourContext.process() {
        onCommand("start") { info ->
            launch {
                while (true) {
                    val message = repeatUntilSome {
                        consumer.poll(400.milliseconds.toJavaDuration()).map { java.lang.String(it.value()) }
                            .firstOrNull()
                    }
                    sendTextMessage(info.chat, "$message")
                }
            }
            api.getDataBaseList(info.chat.id.chatId).fold(
                onSuccess = { databases ->
                    if (databases.isNotEmpty()) {
                        sendTextMessage(
                            info.chat,
                            "Базы данных загружены",
                            replyMarkup = ConstantsKeyboards.getDataBasesKeyBoard(databases.map { it.name })
                        )
                    } else {
                        val name = waitText(
                            SendTextMessage(
                                info.chat.id,
                                "Приветствую! Добавьте вашу первую базу данных для начала работы. Введите имя для базы данных",
                            )
                        ).first().text
                        val host = waitText(
                            SendTextMessage(
                                info.chat.id,
                                "Введите host",
                            )
                        ).first().text
                        val port = waitText(
                            SendTextMessage(
                                info.chat.id,
                                "Введите port",
                            )
                        ).first().text
                        val database = waitText(
                            SendTextMessage(
                                info.chat.id,
                                "Введите database",
                            )
                        ).first().text
                        val username = waitText(
                            SendTextMessage(
                                info.chat.id,
                                "Введите username для подключения к базе данных",
                            )
                        ).first().text
                        val password = waitText(
                            SendTextMessage(
                                info.chat.id,
                                "Введите password для подключения к базе данных",
                            )
                        ).first().text
                        sendTextMessage(info.chat, "Подождите, пробуем получить доступ...")
                        api.sendConfig(info.chat.id.chatId, name, host, port, database, username, password).fold(
                            onSuccess = {
                                RuntimeStorage.userRealtimeMap[info.chat.id.chatId] = false
                                sendTextMessage(
                                    info.chat,
                                    "Доступ успешно получен",
                                    replyMarkup = ConstantsKeyboards.getDataBasesKeyBoard(
                                        api.getDataBaseList(info.chat.id.chatId).fold(onSuccess = { it.map { it.name } }, onFailure = { listOf() })
                                    )
                                )
                            },
                            onFailure = {
                                sendTextMessage(
                                    info.chat,
                                    "Ошибка получения доступа, попробуйте снова добавить базу данных",
                                    replyMarkup = ConstantsKeyboards.getDataBasesKeyBoard(
                                        api.getDataBaseList(info.chat.id.chatId).fold(onSuccess = { it.map { it.name } }, onFailure = { listOf() })
                                    )
                                )
                            }
                        )
                    }
                },
                onFailure = {
                    sendTextMessage(
                        info.chat,
                        "Ошибка получения доступа, попробуйте снова добавить базу данных",
                        replyMarkup = ConstantsKeyboards.onlyAddDatabase
                    )
                }
            )
        }
    }
}