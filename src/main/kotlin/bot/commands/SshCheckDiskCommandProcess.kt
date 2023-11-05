package bot.commands

import bot.CommandWithDataDataBase
import bot.constants.ConstantsKeyboards
import data.Api
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.queries.callback.MessageDataCallbackQuery

class SshCheckDiskCommandProcess(
    val api: Api
) : CommandWithDataDataBase {
    override suspend fun BehaviourContext.process(data: MessageDataCallbackQuery,connection : String) {
        api.sshDiskSpace(data.message.chat.id.chatId).fold(
            onSuccess = { response ->
                sendTextMessage(
                    data.message.chat.id,
                    "Операция выполнена",
                    replyMarkup = ConstantsKeyboards.getSsh(connection)
                )
            },
            onFailure = { error ->
                sendTextMessage(
                    data.message.chat.id,
                    "Ошибка выполнения",
                    replyMarkup = ConstantsKeyboards.getSsh(connection)
                )
            })
    }
}