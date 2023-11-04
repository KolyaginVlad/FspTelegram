package bot.commands

import bot.CommandWithDataDataBase
import bot.constants.ConstantsKeyboards
import data.Api
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import dev.inmo.tgbotapi.types.queries.callback.MessageDataCallbackQuery
import kotlinx.coroutines.flow.first

class AddQueryCommandProcess(
    private val api: Api
): CommandWithDataDataBase {
    override suspend fun BehaviourContext.process(data: MessageDataCallbackQuery, database: String) {
        val name = waitText(
            SendTextMessage(
                data.message.chat.id,
                "Введите имя запроса",
            )
        ).first().text

        val query = waitText(
            SendTextMessage(
                data.message.chat.id,
                "Введите запрос с заполнителями &s для строки и &i для числа",
            )
        ).first().text

        api.createQuery(name, query, data.message.chat.id.chatId, database).fold(
            onSuccess = {
                sendTextMessage(
                    data.message.chat, "Ссылка $name($query) успешно добавлена",
                    replyMarkup = ConstantsKeyboards.getDataBasesCommands(database)
                )
            },
            onFailure = {
                sendTextMessage(
                    data.message.chat, "Ошибка получения доступа",
                    replyMarkup = ConstantsKeyboards.getDataBasesKeyBoard(
                        api.getDataBaseList(data.message.chat.id.chatId)
                            .fold(onSuccess = { it.map { it.name } }, onFailure = { listOf() })
                    )
                )
            }
        )
    }
}