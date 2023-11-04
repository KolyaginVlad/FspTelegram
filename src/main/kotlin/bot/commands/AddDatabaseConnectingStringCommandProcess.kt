package bot.commands

import bot.CommandWithData
import bot.constants.ConstantsKeyboards
import data.Api
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import dev.inmo.tgbotapi.types.queries.callback.MessageDataCallbackQuery
import kotlinx.coroutines.flow.first

class AddDatabaseConnectingStringCommandProcess(
    private val api: Api
): CommandWithData {
    override suspend fun BehaviourContext.process(data: MessageDataCallbackQuery) {
        val connectionString = waitText(
            SendTextMessage(
                data.message.chat.id,
                "Введите строку для подключения",
            )
        ).first().text
        sendTextMessage(data.message.chat.id, "Подождите, пробуем получить доступ...")
        api.connectByConnectionString(data.message.chat.id.chatId, connectionString).fold(
            onSuccess = {
                sendTextMessage(
                    data.message.chat.id,
                    "Доступ успешно получен",
                    replyMarkup = ConstantsKeyboards.empty
                )
            },
            onFailure = {
                sendTextMessage(
                    data.message.chat.id,
                    "Ошибка получения доступа, попробуйте снова добавить базу данных",
                    replyMarkup = ConstantsKeyboards.empty
                )
            }
        )
    }
}