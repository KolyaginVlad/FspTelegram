package bot.commands

import bot.CommandWithDataDataBase
import bot.constants.ConstantsKeyboards
import data.Api
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.queries.callback.MessageDataCallbackQuery

class GetLinksCommandProcess(
    private val api: Api
): CommandWithDataDataBase {
    override suspend fun BehaviourContext.process(data: MessageDataCallbackQuery, database: String) {
        api.link(data.message.chat.id.chatId, database).fold(
            onSuccess = {
                sendTextMessage(
                    data.message.chat.id,
                    "Выберите ссылку",
                    replyMarkup = ConstantsKeyboards.getLinkKeyboard(it, database)
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