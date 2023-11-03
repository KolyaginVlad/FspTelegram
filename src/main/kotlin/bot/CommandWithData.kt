package bot

import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.queries.callback.MessageDataCallbackQuery

interface CommandWithData {
    suspend fun start(context: BehaviourContext, data: MessageDataCallbackQuery) {
        with(context) {
            process(data)
        }
    }

    suspend fun BehaviourContext.process(data: MessageDataCallbackQuery)
}