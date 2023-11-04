package bot.commands

import bot.CommandWithDataDataBase
import data.Api
import data.foldMsg
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.queries.callback.MessageDataCallbackQuery

class KillTransactionCommandProcess(
    private val api: Api
) : CommandWithDataDataBase {
    override suspend fun BehaviourContext.process(data: MessageDataCallbackQuery, database: String) {
        api.killTransaction(data.message.chat.id.chatId)
            .foldMsg(this, data, api, database)
    }
}
