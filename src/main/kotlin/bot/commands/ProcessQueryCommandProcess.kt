package bot.commands

import bot.CommandWithDataDataBase
import bot.constants.ConstantsKeyboards
import bot.constants.ConstantsString
import data.Api
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.queries.callback.MessageDataCallbackQuery

class ProcessQueryCommandProcess(
    private val api: Api
): CommandWithDataDataBase {
    override suspend fun BehaviourContext.process(data: MessageDataCallbackQuery, database: String) {
        api.getQueryByName(data.data.split(ConstantsString.DELIMITER)[2], data.message.chat.id.chatId, database).fold(
            onSuccess = {
                api.executeQuery(data.message.chat.id.chatId, database, it).fold(
                    onSuccess = {
                        sendTextMessage(
                            data.message.chat.id,
                            "Успешно отправлено",
                            replyMarkup = ConstantsKeyboards.getDataBasesKeyBoard(
                                api.getDataBaseList(data.message.chat.id.chatId)
                                    .fold(onSuccess = { it.map { it.name } }, onFailure = { listOf() })
                            )
                        )
                    },
                    onFailure = { error ->
                        error.printStackTrace()
                        sendTextMessage(
                            data.message.chat.id,
                            "Ошибка выполнения запроса",
                            replyMarkup = ConstantsKeyboards.getDataBasesKeyBoard(
                                api.getDataBaseList(data.message.chat.id.chatId)
                                    .fold(onSuccess = { it.map { it.name } }, onFailure = { listOf() })
                            )
                        )
                    }
                )
            },
            onFailure = { error ->
                error.printStackTrace()
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