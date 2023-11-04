package bot.commands

import bot.CommandWithDataDataBase
import bot.constants.ConstantsKeyboards
import data.Api
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.queries.callback.MessageDataCallbackQuery

class ShowMemoryCommandProcess(
    private val api: Api
) : CommandWithDataDataBase {
    override suspend fun BehaviourContext.process(data: MessageDataCallbackQuery, database: String) {
        api.getMemory(data.message.chat.id.chatId, database).fold(
            onFailure = {
                sendTextMessage(
                    data.message.chat.id,
                    "Ошибка получения размера памяти",
                    replyMarkup = ConstantsKeyboards.getMemory(database)
                )
            },
            onSuccess = {
                sendTextMessage(
                    data.message.chat.id,
                    it.toString(),
                    replyMarkup = ConstantsKeyboards.getMemory(database)
                )
            })
    }
}