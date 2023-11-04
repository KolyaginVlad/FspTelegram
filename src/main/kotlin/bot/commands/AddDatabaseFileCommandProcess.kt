package bot.commands

import bot.CommandWithData
import bot.constants.ConstantsKeyboards
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitDocument
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import dev.inmo.tgbotapi.types.queries.callback.MessageDataCallbackQuery
import kotlinx.coroutines.flow.first

class AddDatabaseFileCommandProcess: CommandWithData {
    override suspend fun BehaviourContext.process(data: MessageDataCallbackQuery) {
        val file = waitDocument {
            SendTextMessage(
                data.message.chat.id,
                "Введите добавьте файл подключения",
            )
        }.first()
        sendTextMessage(data.message.chat.id, "Подождите, пробуем получить доступ...")
        sendTextMessage(
            data.message.chat.id,
            "Ошибка получения доступа, попробуйте снова добавить базу данных",
            replyMarkup = ConstantsKeyboards.empty
        )
    }
}