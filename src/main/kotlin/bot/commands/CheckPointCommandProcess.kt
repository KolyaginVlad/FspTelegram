package bot.commands

import bot.CommandWithDataDataBase
import bot.constants.ConstantsKeyboards
import bot.constants.ConstantsString
import data.Api
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import dev.inmo.tgbotapi.types.queries.callback.MessageDataCallbackQuery
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class CheckPointCommandProcess(
    private val api: Api
) : CommandWithDataDataBase {
    override suspend fun BehaviourContext.process(data: MessageDataCallbackQuery, database: String) {

        api.checkPoint(data.message.chat.id.chatId, database).fold(
            onSuccess = { responses ->
                responses.forEach { response ->
                    println("checkPoint $response")
                    when (response.messageType) {
                        "Ok" -> sendTextMessage(
                            data.message.chat.id,
                            "База данных в порядке",
                            // replyMarkup = ConstantsKeyboards.getDataBasesCommands(database)
                        )

                        "Lock" -> sendTextMessage(
                            data.message.chat.id,
                            """
                            Обнаружен deadlock в базе данных ${response.dataBase}!
                            PID: ${response.pid}
                            Последнее время обновления состояния: ${response.stateLastChangeDate}
                            """.trimIndent(),
                            replyMarkup = ConstantsKeyboards.repairTransactionKeyboard(response.dataBase!!)
                        )

                        else -> sendTextMessage(
                            data.message.chat.id,
                            """
                            Обнаружен неизвестная ошибка в базе данных ${response.dataBase}!
                            PID: ${response.pid}
                            Последнее время обновления состояния: ${response.stateLastChangeDate}
                            """.trimIndent(),
                           // replyMarkup = ConstantsKeyboards.getDataBasesCommands(database)
                        )
                    }
                }
                sendTextMessage(
                    data.message.chat.id,
                    "Выберите действие",
                    replyMarkup = ConstantsKeyboards.getDataBasesCommands(database)
                )
            },
            onFailure = { error ->
                sendTextMessage(
                    data.message.chat.id,
                    "Ошибка получения доступа",
                    replyMarkup = ConstantsKeyboards.getDataBasesKeyBoard(
                        api.getDataBaseList(data.message.chat.id.chatId)
                            .fold(onSuccess = { it.map { it.name } }, onFailure = { listOf() })
                    )
                )
            }
        )
    }

}

class CheckPointDateCommandProcess(
    private val api: Api
) : CommandWithDataDataBase {
    override suspend fun BehaviourContext.process(data: MessageDataCallbackQuery, database: String) {
        runBlocking {
            val date = waitText(
                SendTextMessage(
                    data.message.chat.id,
                    ConstantsString.enterDate,
                )
            ).first().text
            api.checkPointOnDate(data.message.chat.id.chatId, database, date).fold(
                onSuccess = { response ->
                    sendTextMessage(
                        data.message.chat.id,
                        response.toString(),
                        replyMarkup = ConstantsKeyboards.getDataBasesCommands(database)
                    )
                },
                onFailure = { error ->
                    sendTextMessage(
                        data.message.chat.id,
                        "Ошибка получения доступа",
                        replyMarkup = ConstantsKeyboards.getDataBasesKeyBoard(
                            api.getDataBaseList(data.message.chat.id.chatId)
                                .fold(onSuccess = { it.map { it.name } }, onFailure = { listOf() })
                        )
                    )
                }
            )
        }
    }
}
