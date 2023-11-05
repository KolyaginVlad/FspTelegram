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

class AddSshQueryCommandProcess(
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
                "Введите запрос",
            )
        ).first().text

        api.createSshQuery(name, query, data.message.chat.id.chatId, database).fold(
            onSuccess = {
                sendTextMessage(
                    data.message.chat, "Запрос успешно создан",
                    replyMarkup = ConstantsKeyboards.getSsh(database)
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