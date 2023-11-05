package bot.commands

import bot.CommandWithDataDataBase
import bot.constants.ConstantsKeyboards
import data.Api
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.queries.callback.MessageDataCallbackQuery

class SshTcpDumpCommandProcess(
    val api: Api
) : CommandWithDataDataBase {
    override suspend fun BehaviourContext.process(data: MessageDataCallbackQuery, database: String) {
        api.sshTcpdump(data.message.chat.id.chatId).fold(
            onSuccess = { response ->
                sendTextMessage(
                    data.message.chat.id,
                    "Операция выполнена",
                    replyMarkup = ConstantsKeyboards.getSsh(database)
                )
            },
            onFailure = { error ->
                sendTextMessage(
                    data.message.chat.id,
                    "Ошибка выполнения",
                    replyMarkup = ConstantsKeyboards.getSsh(database)
                )
            })
    }
}