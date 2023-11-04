package bot.commands

import Dependencies
import bot.CommandWithDataDataBase
import bot.RuntimeStorage
import bot.constants.ConstantsKeyboards
import data.Api
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.queries.callback.MessageDataCallbackQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.kodein.di.instance

class OnCheckPointRealtimeCommandProcess(
    private val api: Api,
    private val corutineScope: CoroutineScope,
    private val jsonParser: Json
) : CommandWithDataDataBase {
    private val metrixCommand: MetrixCommand by Dependencies.di.instance()
    override suspend fun BehaviourContext.process(data: MessageDataCallbackQuery, database: String) {
        RuntimeStorage.userRealtimeMap[data.message.chat.id.chatId] = true
        val context = this
        corutineScope.launch {
            while (RuntimeStorage.userRealtimeMap[data.message.chat.id.chatId] == true) {
                api.checkPoint(data.message.chat.id.chatId, database).fold(
                    onSuccess = { responses ->
                        responses.forEach { response ->
                            when (response.messageType) {
                                "Ok" -> api.getMetrix(database, data.message.chat.id.chatId).fold(
                                    onSuccess = { response ->
                                        sendTextMessage(
                                            data.message.chat.id,
                                            response.toString(),
                                            replyMarkup = ConstantsKeyboards.stop
                                        )
                                    },
                                    onFailure = { error ->
                                        sendTextMessage(
                                            data.message.chat.id,
                                            "База данных в порядке, невозможно получить метрики",
                                            replyMarkup = ConstantsKeyboards.stop
                                        )
                                    }
                                )

                                "Lock" -> sendTextMessage(
                                    data.message.chat.id,
                                    """
                            Обнаружен deadlock в базе данных ${response.dataBase}!
                            PID: ${response.pid}
                            Последнее время обновления состояния: ${response.stateLastChangeDate}
                            """.trimIndent(),
                                    replyMarkup = ConstantsKeyboards.repairTransactionKeyboard(
                                        response.dataBase!!,
                                    )
                                )

                                else -> sendTextMessage(
                                    data.message.chat.id,
                                    """
                            Обнаружен неизвестная ошибка в базе данных ${response.dataBase}!
                            PID: ${response.pid}
                            Последнее время обновления состояния: ${response.stateLastChangeDate}
                            """.trimIndent(),
                                    replyMarkup = ConstantsKeyboards.getDataBasesCommands(database)
                                )
                            }
                        }
                    }, onFailure = { throwable ->
                        sendTextMessage(
                            data.message.chat.id,
                            "Статус не получен",
                        )
                    })
                delay(15000)
            }
        }
    }
}