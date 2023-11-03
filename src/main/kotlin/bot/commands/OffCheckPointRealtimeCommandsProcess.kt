package bot.commands

import bot.Command
import bot.RuntimeStorage
import bot.constants.ConstantsKeyboards
import bot.constants.ConstantsSting
import data.Api
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onText

class OffCheckPointRealtimeCommandsProcess(
    private val api: Api
) : Command {
    override suspend fun BehaviourContext.process() {
        onText({
            it.content.text == ConstantsSting.onRealTime
        }) {
            RuntimeStorage.userRealtimeMap[it.chat.id.chatId] = false
            sendTextMessage(
                it.chat,
                ConstantsSting.realtimeOff,
                replyMarkup = ConstantsKeyboards.onlyAddDatabase
            )
        }
    }
}