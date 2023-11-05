package bot.commands

import bot.CommandWithDataDataBase
import bot.constants.ConstantsKeyboards
import data.Api
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.queries.callback.MessageDataCallbackQuery

class UpdateSshQueryCommandProcess(
    private val api: Api
): CommandWithDataDataBase {
    override suspend fun BehaviourContext.process(data: MessageDataCallbackQuery, database: String) {
        api.getSshQueries(
            data.message.chat.id.chatId,
            database
        ).fold(
            onSuccess = { queries ->
                if (queries.isNotEmpty()) {
                    sendTextMessage(
                        data.message.chat.id,
                        "Выберите скрипт для обновления",
                        replyMarkup = ConstantsKeyboards.updateSshQuery(database, queries)
                    )
                } else {
                    sendTextMessage(
                        data.message.chat.id,
                        "У вас нет скриптов",
                        replyMarkup = ConstantsKeyboards.getSshCrud(database)
                    )
                }
            },
            onFailure = {
                it.printStackTrace()
                sendTextMessage(
                    data.message.chat.id,
                    "Проблемы с доступом к скриптам, попробуйте позже",
                    replyMarkup = ConstantsKeyboards.getDataBasesCommands(database)
                )
            }
        )
    }
}