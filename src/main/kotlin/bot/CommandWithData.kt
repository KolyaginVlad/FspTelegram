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

interface CommandWithDataDataBase {
    suspend fun start(context: BehaviourContext, data: MessageDataCallbackQuery, database: String) {
        with(context) {
            process(data, database)
        }
    }

    suspend fun BehaviourContext.process(data: MessageDataCallbackQuery, database: String)
}

interface CommandWithDataDataBaseAndParam {
    suspend fun start(context: BehaviourContext, data: MessageDataCallbackQuery, database: String, param: String) {
        with(context) {
            process(data, database, param)
        }
    }

    suspend fun BehaviourContext.process(data: MessageDataCallbackQuery, database: String, param: String)
}