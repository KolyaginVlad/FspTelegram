package bot.commands

import bot.CommandWithDataDataBaseAndParam
import bot.constants.ConstantsKeyboards
import data.Api
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import dev.inmo.tgbotapi.types.queries.callback.MessageDataCallbackQuery
import kotlinx.coroutines.flow.first

class ChangeMemoryCommandProcess(
    private val api: Api
) : CommandWithDataDataBaseAndParam {
    override suspend fun BehaviourContext.process(data: MessageDataCallbackQuery, database: String, param: String) {
        api.getMemory(data.message.chat.id.chatId, database).fold(
            onFailure = {
                sendTextMessage(
                    data.message.chat.id,
                    "Ошибка получения размера памяти",
                    replyMarkup = ConstantsKeyboards.getMemory(database)
                )
            },
            onSuccess = {
                val newParams = it
                val value = waitText(
                    SendTextMessage(
                        data.message.chat.id,
                        "Введите новое значение",
                    )
                ).first().text
                when (param) {
                    "1" -> {
                        newParams.maintenanceWorkMemory = value
                    }

                    "2" -> {
                        newParams.memoryLimit = value
                    }

                    "3" -> {
                        newParams.workMemory = value
                    }

                    "4" -> {
                        newParams.effectiveCacheSize = value
                    }
                }
                api.changeMemory(
                    userId = data.message.chat.id.chatId,
                    dataBase = database,
                    effectiveCacheSize = newParams.effectiveCacheSize,
                    maintenanceWorkMemory = newParams.maintenanceWorkMemory,
                    workMemory = newParams.workMemory,
                    memoryLimit = newParams.memoryLimit
                ).fold(
                    onSuccess = {
                        sendTextMessage(
                            data.message.chat.id,
                            "Выберите какой ещё параметр хотите изменить",
                            replyMarkup = ConstantsKeyboards.getChangeMemory(database)
                        )
                    }, onFailure = {
                        sendTextMessage(
                            data.message.chat.id,
                            "Не удалось изменить значение",
                            replyMarkup = ConstantsKeyboards.getMemory(database)
                        )
                    }
                )
            }
        )
    }
}